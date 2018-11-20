package br.com.leandroferreira.webfluxfirst.higherwebflux.router

import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.DefaultServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.exceptions.RouteNotFoundException
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.HandlerFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.RequestPredicateFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.WebHandlerFn
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.REQUEST_ATTRIBUTE
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.core.publisher.Mono

private val REQUEST_ATTRIBUTE = "${RouterFunctions::class.java.name}.request"

fun <F, T : ServerResponseK> createRouteKFn(
    predicate: RequestPredicateFn,
    handle: HandlerFn<F, T>
) : RouterFn<F, T> =
    { apError, request ->
        if(predicate(request)) {
            apError.just(handle)
        } else {
            apError.raiseError(Unit)
        }
    }

fun <F, T : ServerResponseK> nest(predicate: RequestPredicateFn, routerFunction: RouterFn<F, T>): RouterFn<F, T> =
    { async, request ->
        if(predicate(request)) {
            routerFunction(async, request)
        } else {
            async.raiseError(Unit)
        }
    }

//override fun route(serverRequest: ServerRequest): Mono<HandlerFunction<T>> {
//    return this.predicate.nest(serverRequest)
//        .map({ nestedRequest ->
//            if (logger.isTraceEnabled()) {
//                val logPrefix = serverRequest.exchange().logPrefix
//                logger.trace(logPrefix + String.format("Matched nested %s", this.predicate))
//            }
//            this.routerFunction.route(nestedRequest)
//                .doOnNext({ match ->
//                    if (nestedRequest !== serverRequest) {
//                        serverRequest.attributes().clear()
//                        serverRequest.attributes()
//                            .putAll(nestedRequest.attributes())
//                    }
//                })
//        }
//        ).orElseGet(Supplier<Mono<HandlerFunction<T>>> { Mono.empty() })
//}

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