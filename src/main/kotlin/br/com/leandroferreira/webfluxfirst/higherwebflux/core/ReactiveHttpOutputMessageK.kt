package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpMessage
import reactor.core.publisher.Mono
import java.util.function.Supplier

interface ReactiveHttpOutputMessageK : HttpMessage {

    val isCommitted: Boolean

    fun bufferFactory(): DataBufferFactory

    fun beforeCommit(action: Supplier<out Mono<Void>>)

    //TODO: There's no Publisher, deal with it later
//    fun writeWith(body: Publisher<out DataBuffer>): Mono<Void>
//
//    fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void>

    fun <F> setComplete(async: Async<F>) : Kind<F, Unit>

}