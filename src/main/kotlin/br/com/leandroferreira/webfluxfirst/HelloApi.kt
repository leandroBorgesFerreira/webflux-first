package br.com.leandroferreira.webfluxfirst

import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.body
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.RouterFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.createRouteKFn
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.requestPredicates.GetK
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.HandlerFn
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

const val BASE_PATH = "/hi"

fun hiRoutes() = RouterFunctions.route(GET(BASE_PATH), HandlerFunction { getHello() })

private fun getHello(): Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("Hi!"))

fun <F> hiRoutesKFn(): RouterFn<F, ServerResponseK> = createRouteKFn(GetK(BASE_PATH), handleRequestFn())

fun <F> handleRequestFn(): HandlerFn<F, ServerResponseK> = { async, request ->
  ServerResponseK.ok().body(async) //Todo: Fix this: It should be async.just(something)... Or maybe not o.O
}

//private fun <F> getHelloK(async: Async<F>): Kind<F, ServerResponseK> {
//    ServerResponseK.ok(
//}
