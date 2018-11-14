package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.WebHandlerFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.DefaultServerRequestK
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.REQUEST_ATTRIBUTE
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

typealias HandlerFn<F, T> = (async: Async<F>, request: ServerRequest) -> Kind<F, T>

private val REQUEST_ATTRIBUTE = "${RouterFunctions::class.java.name}.request"

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

fun <F> toHttpHandler(routerFunction: RouterFn<F, *>): HttpHandler {
    return toHttpHandler(routerFunction, HandlerStrategies.withDefaults())
}

fun <F> toHttpHandler(routerFunction: RouterFn<F, *>, strategies: HandlerStrategies): HttpHandler {
    val webHandler = toWebHandler(routerFunction, strategies)
    return WebHttpHandlerBuilder.webHandler(webHandler)
        .filters { filters -> filters.addAll(strategies.webFilters()) }
        .exceptionHandlers { handlers -> handlers.addAll(strategies.exceptionHandlers()) }
        .localeContextResolver(strategies.localeContextResolver())
        .build()
}

fun <F> toWebHandler(routerFunction: RouterFn<F, *>, strategies: HandlerStrategies): WebHandlerFn<F> {
    return { exchange ->
        val request = DefaultServerRequestK(exchange, strategies.messageReaders())
        addAttributes(exchange, request)

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