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

private fun <T> cast(messageWriter: HttpMessageWriterK<*>): HttpMessageWriterK<T> = messageWriter as HttpMessageWriterK<T>

private fun <F, P : Async<F>, M : ReactiveHttpOutputMessage> writeWithMessageWritersK(
    outputMessage: M,
    context: BodyInserterContext,
    body: P,
    bodyType: ResolvableType
): Kind<F, Unit> =
    outputMessage.headers.contentType.let { mediaType ->
        context.messageWriters()
            .asSequence()
            .filter { messageWriter -> messageWriter.canWrite(bodyType, mediaType) }
            .first()
            .toOption()
            .map { httpMessageWriter -> cast<Any>(httpMessageWriter) }
            .map { writer -> writeK(body, bodyType, mediaType, outputMessage, context, writer) }
            .getOrElse {
                body.raiseError(unsupportedError(bodyType, context, mediaType))
            }
    }

private fun <F, T> writeK(
    input: Async<F>,
    type: ResolvableType,
    @Nullable mediaType: MediaType?,
    message: ReactiveHttpOutputMessage,
    context: BodyInserterContext,
    writer: HttpMessageWriterK<T>
): Kind<F, Unit> =
    context.serverRequest()
        .map { request ->
            val response = message as ServerHttpResponse
            writer.write(input, type, type, mediaType, request, response, context.hints())
        }
        .getOrElse { writer.write(input, type, mediaType, message, context.hints()) }

private fun unsupportedError(
    bodyType: ResolvableType,
    context: BodyInserterContext,
    @Nullable mediaType: MediaType?
): UnsupportedMediaTypeException =
    context.messageWriters()
        .asSequence()
        .flatMap { reader -> reader.getWritableMediaTypes().asSequence() }
        .toList().let { supportedMediaTypes ->
            UnsupportedMediaTypeException(mediaType, supportedMediaTypes, bodyType)
        }