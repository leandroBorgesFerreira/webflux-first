package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.effects.typeclasses.Async
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.web.reactive.function.UnsupportedMediaTypeException

private fun <T> cast(messageWriter: HttpMessageWriterK<*>): HttpMessageWriterK<T> = messageWriter as HttpMessageWriterK<T>

//Todo: This one looks weird. Take a look here in a later moment.
fun <F, T: Async<F>> fromAsync(async: T, elementClass: Class<T>?) : BodyInserterKFn<F, ReactiveHttpOutputMessageK> = {
    message, context ->
        writeWithMessageWritersK(message, context, async, ResolvableType.forClass(elementClass))
}

private fun <F, P : Async<F>, M : ReactiveHttpOutputMessageK> writeWithMessageWritersK(
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
    message: ReactiveHttpOutputMessageK,
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