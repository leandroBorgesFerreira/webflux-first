package br.com.leandroferreira.webfluxfirst

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.HandlerFunctionK
import br.com.leandroferreira.webfluxfirst.higherwebflux.RouterFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.createRouteK
import br.com.leandroferreira.webfluxfirst.higherwebflux.createRouteKFn
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

const val BASE_PATH = "/hi"

fun hiRoutes() = RouterFunctions.route(GET(BASE_PATH), HandlerFunction { getHello() })

fun hiRoutesK() = createRouteK(GET(BASE_PATH), object : HandlerFunctionK<ServerResponse> {
    override fun <F> handle(async: Async<F>, request: ServerRequest): Kind<F, ServerResponse> =
        getHello() //Return something valid here!
})

fun <F> hiRoutesKFn(): RouterFn<F, ServerResponse> = createRouteKFn(GET(BASE_PATH), { async, request ->
    async.just(ServerResponse.ok().) //return something valid here!!
})

private fun getHello(): Mono<ServerResponse> =
    ServerResponse.ok().body(Mono.just("Hi!"))

