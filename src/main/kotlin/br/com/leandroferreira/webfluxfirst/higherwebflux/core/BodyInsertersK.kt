package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.effects.typeclasses.Async
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.web.reactive.function.UnsupportedMediaTypeException
import reactor.core.publisher.Mono

private fun <F, P : Publisher<*>, M : ReactiveHttpOutputMessage> writeWithMessageWritersK(
    async: Async<F>,
    outputMessage: M,
    context: BodyInserterContext,
    body: P,
    bodyType: ResolvableType
): Kind<F, Void> {
    return outputMessage.headers.contentType.let { mediaType ->
        context.messageWriters()
            .asSequence()
            .filter { messageWriter -> messageWriter.canWrite(bodyType, mediaType) }
            .first()
            .toOption()
            .map { httpMessageWriter -> cast<Any>(httpMessageWriter) }
            .map { writer -> write(body, bodyType, mediaType, outputMessage, context, writer) }
            .getOrElse { Mono.error<Void>(unsupportedError(bodyType, context, mediaType)) }
    }
}

private fun <T> write(
    input: Publisher<out T>, type: ResolvableType,
    @Nullable mediaType: MediaType?, message: ReactiveHttpOutputMessage,
    context: BodyInserterContext, writer: HttpMessageWriter<T>)
    : Mono<Void> {
    return context.serverRequest()
        .map { request ->
            val response = message as ServerHttpResponse
            writer.write(input, type, type, mediaType, request, response, context.hints())
        }
        .getOrElse { writer.write(input, type, mediaType, message, context.hints()) }
}

private fun <T> cast(messageWriter: HttpMessageWriter<*>): HttpMessageWriter<T> {
    return messageWriter as HttpMessageWriter<T>
}

private fun unsupportedError(
    bodyType: ResolvableType,
    context: BodyInserterContext,
    @Nullable mediaType: MediaType?
): UnsupportedMediaTypeException =
    context.messageWriters()
        .asSequence()
        .flatMap { reader -> reader.writableMediaTypes.asSequence() }
        .toList().let { supportedMediaTypes ->
            UnsupportedMediaTypeException(mediaType, supportedMediaTypes, bodyType)
        }