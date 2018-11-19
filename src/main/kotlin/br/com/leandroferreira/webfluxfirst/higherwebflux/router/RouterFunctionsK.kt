package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.DefaultServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.exceptions.RouteNotFoundException
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.WebHandlerFn
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.REQUEST_ATTRIBUTE
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

typealias HandlerFn<F, T> = (async: Async<F>, request: ServerRequestK) -> Kind<F, T>

private val REQUEST_ATTRIBUTE = "${RouterFunctions::class.java.name}.request"

fun <F, T : ServerResponseK> createRouteKFn(
    predicate: RequestPredicate,
    handle: HandlerFn<F, T>
) : RouterFn<F, T> {
    return { apError, request ->
        if(predicate.test(request)) {
            apError.just(handle)
        } else {
            apError.raiseError(Unit)
        }
    }

}

fun <F> toHttpHandler(routerFn: RouterFn<F, *>): HttpHandler = toHttpHandler(routerFn, HandlerStrategies.withDefaults())

fun <F> toHttpHandler(async: Async<F>, routerFunction: RouterFn<F, *>, strategies: HandlerStrategies): HttpHandler {
    val webHandler = toWebHandler(async, routerFunction, strategies)
    return WebHttpHandlerBuilder.webHandler(webHandler)
        .filters { filters -> filters.addAll(strategies.webFilters()) }
        .exceptionHandlers { handlers -> handlers.addAll(strategies.exceptionHandlers()) }
        .localeContextResolver(strategies.localeContextResolver())
        .build()
}

fun <F, T> toWebHandler(async: Async<F>, routerFunction: RouterFn<F, *>, strategies: HandlerStrategies): WebHandlerFn<F> {
    return { exchange ->
        val request = DefaultServerRequestK(exchange, strategies.messageReaders())
        addAttributes(exchange, request)
        async.run {

            routerFunction(this, request).handleError { throwable ->
                val blaa = if (throwable is RouteNotFoundException) {
                    just ({ _: Async<F>, _: ServerRequestK -> ServerResponseK.notFound().build(this) })
                } else {
                    raiseError(throwable)
                }

                blaa
            }.map {

            }
        }


        async.just(Unit)
//        val request = DefaultServerRequest(exchange, strategies.messageReaders())
//        addAttributes(exchange, request)
//        routerFunction.route(request)
//            .defaultIfEmpty(notFound<*>())
//            .flatMap<*> { handlerFunction -> wrapException<*> { handlerFunction.handle(request) } }
//            .flatMap<Void> { response ->
//                wrapException<Void> {
//                    response.writeTo(exchange,
//                        HandlerStrategiesResponseContext(strategies))
//                }
//            }
    }
}

private fun addAttributes(exchange: ServerWebExchange, request: ServerRequestK) {
    val attributes = exchange.attributes
    attributes[REQUEST_ATTRIBUTE] = request
}