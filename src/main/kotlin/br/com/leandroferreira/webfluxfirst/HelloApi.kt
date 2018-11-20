package br.com.leandroferreira.webfluxfirst

import arrow.Kind
import arrow.effects.typeclasses.Async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.body
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.RouterFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.createRouteKFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.requestPredicates.GetK
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

const val BASE_PATH = "/hi"

fun hiRoutes() = RouterFunctions.route(GET(BASE_PATH), HandlerFunction { getHello() })

fun <F> hiRoutesKFn(): RouterFn<F, ServerResponseK> = createRouteKFn(GetK(BASE_PATH)) { async, request ->
    ServerResponseK.ok().body(async) //Todo: Fix this: It should be async.just(something)... Or maybe not o.O
}

private fun getHello(): Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("Hi!"))

private fun <F> getHelloK(async: Async<F>): Kind<F, ServerResponseK> {
    ServerResponseK.ok().b
}

//fun hiRoutesK() = createRouteK(GET(BASE_PATH), object : HandlerFunctionK<ServerResponse> {
//    override fun <F> handle(async: Async<F>, request: ServerRequest): Kind<F, ServerResponse> =
//        getHello() //Return something valid here!
//})

