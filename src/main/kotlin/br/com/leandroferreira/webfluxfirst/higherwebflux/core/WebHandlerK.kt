package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import org.springframework.web.server.ServerWebExchange

typealias WebHandlerFn<F> = (exchange: ServerWebExchange) -> Kind<F, Unit>