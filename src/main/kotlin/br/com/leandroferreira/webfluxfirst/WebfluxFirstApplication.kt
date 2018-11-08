package br.com.leandroferreira.webfluxfirst

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

    val routerFunctionF : RouterFunction<ServerResponse> =
        RouterFunctions.nest({ request -> request.path() == "/api" }, { _ ->
            Mono.just(HandlerFunction { request ->
                val mono : Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("hi"))
                mono
            })
        })


    val httpHandler: HttpHandler = RouterFunctions.toHttpHandler(routerFunctionF)
    val adapter = ReactorHttpHandlerAdapter(httpHandler)

    HttpServer.create()
        .host("localhost")
        .port(8081)
        .handle(adapter)
        .bindUntilJavaShutdown(Duration.ofSeconds(30)) { println("It is running!!") }

}
