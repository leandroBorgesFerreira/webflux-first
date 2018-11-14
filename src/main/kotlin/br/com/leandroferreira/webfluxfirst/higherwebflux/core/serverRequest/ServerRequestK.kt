package br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest

import org.springframework.http.HttpCookie
import org.springframework.http.HttpMethod
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.server.PathContainer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyExtractor
import java.net.InetSocketAddress
import java.net.URI
import java.util.*

interface ServerRequestK {
    fun method(): HttpMethod? = HttpMethod.resolve(methodName())

    fun methodName(): String

    fun uri(): URI

    fun path(): String = uri().rawPath

    fun pathContainer(): PathContainer = PathContainer.parsePath(path())

    fun headers(): Headers

    fun cookies(): MultiValueMap<String, HttpCookie>

    fun remoteAddress(): Optional<InetSocketAddress>

    fun messageReaders(): List<HttpMessageReader<*>>

    fun <T> body(extractor: BodyExtractor<T, in ServerHttpRequest>): T

    fun <T> body(extractor: BodyExtractor<T, in ServerHttpRequest>, hints: Map<String, Any>): T

//    fun <T> bodyToMono(elementClass: Class<out T>): Mono<T>
//
//    fun <T> bodyToMono(typeReference: ParameterizedTypeReference<T>): Mono<T>
//
//    fun <T> bodyToFlux(elementClass: Class<out T>): Flux<T>
//
//    fun <T> bodyToFlux(typeReference: ParameterizedTypeReference<T>): Flux<T>

    interface Headers {

    }

}