package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse

interface HttpMessageWriterK<T> {
    fun getWritableMediaTypes(): List<MediaType>

    fun canWrite(elementType: ResolvableType, mediaType: MediaType?): Boolean

    fun <F> write(
        inputStream: Async<F>,
        elementType: ResolvableType,
        mediaType: MediaType?,
        message: ReactiveHttpOutputMessageK,
        hints: Map<String, Any>
    ): Kind<F, Unit>

    fun <F> write(
        inputStream: Async<F>,
        actualType: ResolvableType,
        elementType: ResolvableType,
        mediaType: MediaType?,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        hints: Map<String, Any>
    ): Kind<F, Unit> = write(inputStream, elementType, mediaType, response, hints)
}