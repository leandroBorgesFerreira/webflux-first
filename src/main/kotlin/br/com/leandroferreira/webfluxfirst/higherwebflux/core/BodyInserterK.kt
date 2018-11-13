package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.Async
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.server.reactive.ServerHttpRequest

interface BodyInserterContext {

    /**
     * Return the [HttpMessageWriters][HttpMessageWriter] to be used for response body conversion.
     * @return the stream of message writers
     */
    fun messageWriters(): List<HttpMessageWriterK<*>>

    /**
     * Optionally return the [ServerHttpRequest], if present.
     */
    fun serverRequest(): Option<ServerHttpRequest>

    /**
     * Return the map of hints to use for response body conversion.
     */
    fun hints(): Map<String, Any>

    fun <F, T, M : ReactiveHttpOutputMessage> insert(async: Async<F>, outputMessage: M, context: BodyInserterContext)
        : Kind<F, Unit>
}

typealias BodyInserterKFn<F, M> = (async: Async<F>, outputMessage: M, context: BodyInserterContext) -> Kind<F, Unit>



