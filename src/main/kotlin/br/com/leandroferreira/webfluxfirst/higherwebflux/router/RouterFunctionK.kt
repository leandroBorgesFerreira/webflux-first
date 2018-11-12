package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

//public interface RouterFunction<T extends ServerResponse> {
//
//    fun route(request: ServerRequest): Mono<HandlerFunction<T>>
//
//}

interface RouterFunctionK<T: ServerResponse> {

    fun <F> route(async: Async<F>, request: ServerRequest): Kind<F, HandlerFunctionK<T>>

}

