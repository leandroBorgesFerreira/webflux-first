package br.com.leandroferreira.webfluxfirst.higherwebflux.utils

import arrow.Kind
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.bodyInserter.BodyInserterContext
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import org.springframework.web.server.ServerWebExchange

typealias BodyInserterKFn<F, M> = (outputMessage: M, context: BodyInserterContext) -> Kind<F, Unit>
typealias WebHandlerFn<F> = (exchange: ServerWebExchange) -> Kind<F, Unit>

typealias RequestPredicateFn = (request: ServerRequestK) -> Boolean