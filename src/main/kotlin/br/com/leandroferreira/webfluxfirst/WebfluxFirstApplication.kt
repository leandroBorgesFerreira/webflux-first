package br.com.leandroferreira.webfluxfirst

import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.instances.io.async.async
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.ServerResponseK
import br.com.leandroferreira.webfluxfirst.higherwebflux.router.RouterFn
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.netty.http.server.HttpServer
import java.time.Duration

fun main(args: Array<String>) {
    val routerFunction : RouterFunction<ServerResponse> =
        RouterFunctions.nest(RequestPredicates.path("/api"), hiRoutes())

    val routerFn : RouterFn<ForIO, ServerResponseK> = hiRoutesKFn()

    val httpHandler: HttpHandler = RouterFunctions.toHttpHandler(routerFunction)
    val adapter = ReactorHttpHandlerAdapter(httpHandler)

    HttpServer.create()
        .host("localhost")
        .port(8081)
        .handle(adapter)
        .bindUntilJavaShutdown(Duration.ofSeconds(30)) { println("It is running!!") }

}

val routerFunctionF : RouterFunction<ServerResponse> =
    RouterFunctions.nest({ request -> request.path() == "/api" }, { _ ->
        Mono.just(HandlerFunction { request ->
            val mono : Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("hi"))
            mono
        })
    })