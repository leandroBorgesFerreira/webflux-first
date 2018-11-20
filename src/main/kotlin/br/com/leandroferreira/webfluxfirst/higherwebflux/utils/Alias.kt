package br.com.leandroferreira.webfluxfirst.higherwebflux.utils

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.bodyInserter.BodyInserterContext
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import org.springframework.web.server.ServerWebExchange

typealias BodyInserterKFn<F, M> = (outputMessage: M, context: BodyInserterContext) -> Kind<F, Unit>
typealias WebHandlerFn<F> = (exchange: ServerWebExchange) -> Kind<F, Unit>

typealias RequestPredicateFn = (request: ServerRequestK) -> Boolean
typealias HandlerFn<F, T> = (async: Async<F>, request: ServerRequestK) -> Kind<F, T>