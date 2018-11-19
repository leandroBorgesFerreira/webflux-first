package br.com.leandroferreira.webfluxfirst.higherwebflux.utils

import arrow.Kind
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.bodyInserter.BodyInserterContext
import org.springframework.web.server.ServerWebExchange

typealias BodyInserterKFn<F, M> = (outputMessage: M, context: BodyInserterContext) -> Kind<F, Unit>
typealias WebHandlerFn<F> = (exchange: ServerWebExchange) -> Kind<F, Unit>