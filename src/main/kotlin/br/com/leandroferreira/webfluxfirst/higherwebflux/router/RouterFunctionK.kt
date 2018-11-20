package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.Kind
import arrow.effects.typeclasses.Async
import arrow.typeclasses.ApplicativeError
import arrow.typeclasses.MonadError
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.HandlerFn
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

//public interface RouterFunction<T extends ServerResponse> {
//
//    fun route(request: ServerRequest): Mono<HandlerFunction<T>>
//
//}

typealias RouterFn<F, T> = (async: ApplicativeError<F, Unit>, request: ServerRequestK) -> Kind<F, HandlerFn<F, T>>
