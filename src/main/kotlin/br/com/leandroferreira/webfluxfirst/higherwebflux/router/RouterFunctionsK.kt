package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import java.lang.IllegalStateException

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

typealias HandlerFn<F, T> = (async: Async<F>, request: ServerRequest) -> Kind<F, T>
typealias RouterFn<F, T> = (async: Async<F>, request: ServerRequest) -> Kind<F, HandlerFn<F, T>>

fun <F, T : ServerResponseK> createRouteKFn(
    predicate: RequestPredicate,
    handle: HandlerFn<F, T>
) : RouterFn<F, T> {
    return { async, request ->
        if(predicate.test(request)) {
            async.just(handle)
        } else {
            async.raiseError(IllegalStateException("This route doesn't exist!"))
        }
    }

}