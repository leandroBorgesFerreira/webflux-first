package br.com.leandroferreira.webfluxfirst.higherwebflux

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

fun <T : ServerResponse> createRouteK(
    predicate: RequestPredicate,
    handlerFunctionK: HandlerFunctionK<T>
): RouterFunctionK<T> =
    object : RouterFunctionK<T> {
        override fun <F> route(async: Async<F>, request: ServerRequest): Kind<F, HandlerFunctionK<T>> {
            return if(predicate.test(request)) {
                async.just(handlerFunctionK)
            } else {
                async.raiseError(Exception())
            }
        }
    }

