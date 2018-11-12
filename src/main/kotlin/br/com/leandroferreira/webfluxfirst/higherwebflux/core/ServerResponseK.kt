package br.com.leandroferreira.webfluxfirst.higherwebflux.core

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import java.net.URI
import java.time.ZonedDateTime
import java.util.function.Consumer

interface ServerResponseK {

    companion object {
        fun ok(): BodyBuilderK {
            return status(HttpStatus.OK)
        }

        fun status(status: HttpStatus): BodyBuilderK {
            return DefaultServerResponseKBuilderK(status)
        }
    }

    fun statusCode(): HttpStatus

    fun headers(): HttpHeaders

    fun cookies(): MultiValueMap<String, ResponseCookie>

    fun <F> writeTo(async: Async<F>, exchange: ServerWebExchange, context: ServerResponse.Context): Kind<F, Unit>

}

interface HeadersBuilder<B : HeadersBuilder<B>> {

    fun header(headerName: String, vararg headerValues: String): B

    fun headers(headersConsumer: Consumer<HttpHeaders>): B

    fun cookie(cookie: ResponseCookie): B

    fun cookies(cookiesConsumer: Consumer<MultiValueMap<String, ResponseCookie>>): B

    fun allow(vararg allowedMethods: HttpMethod): B

    fun allow(allowedMethods: Set<HttpMethod>): B

    fun eTag(eTag: String): B

    fun lastModified(lastModified: ZonedDateTime): B

    fun location(location: URI): B

    fun cacheControl(cacheControl: CacheControl): B

    fun varyBy(vararg requestHeaders: String): B

    fun <F> build(async: Async<F>): Kind<F, ServerResponseK>

    /**
     * Build the response entity with a custom writer function.
     * @param writeFunction the function used to write to the [ServerWebExchange]
     */
//    fun build(writeFunction: BiFunction<ServerWebExchange, Context, Mono<Void>>): Mono<ServerResponseK>
}

interface BodyBuilderK : HeadersBuilder<BodyBuilderK> {

    fun contentLength(contentLength: Long): BodyBuilderK

    fun contentType(contentType: MediaType): BodyBuilderK

    fun hint(key: String, value: Any): BodyBuilderK

    fun <F> syncBody(async: Async<F>, body: Any): Kind<F,ServerResponseK>

    fun <F, M: ReactiveHttpOutputMessage> body(async: Async<F>, inserter: BodyInserterKFn<F, M>): Kind<F, ServerResponseK>

    fun <F> render(async: Async<F>, name: String, vararg modelAttributes: Any): Kind<F, ServerResponseK>

    fun <F> render(async: Async<F>, name: String, model: Map<String, *>): Kind<F, ServerResponseK>
}
