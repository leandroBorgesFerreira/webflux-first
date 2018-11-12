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

class DefaultServerResponseKBuilderK(private val statusCode : Int): BodyBuilderK {

    constructor(status: HttpStatus) : this(status.value())

    override fun header(headerName: String, vararg headerValues: String): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun headers(headersConsumer: Consumer<HttpHeaders>): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cookie(cookie: ResponseCookie): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cookies(cookiesConsumer: Consumer<MultiValueMap<String, ResponseCookie>>): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allow(vararg allowedMethods: HttpMethod): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allow(allowedMethods: Set<HttpMethod>): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eTag(eTag: String): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun lastModified(lastModified: ZonedDateTime): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun location(location: URI): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cacheControl(cacheControl: CacheControl): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun varyBy(vararg requestHeaders: String): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <F> build(async: Async<F>): Kind<F, ServerResponseK> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contentLength(contentLength: Long): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contentType(contentType: MediaType): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hint(key: String, value: Any): BodyBuilderK {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <F> syncBody(async: Async<F>, body: Any): Kind<F, ServerResponseK> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <F, M : ReactiveHttpOutputMessage> body(async: Async<F>, inserter: BodyInserterKFn<F, M>): Kind<F, ServerResponseK> {
        return async.just(BodyInserterResponse()) //Todo: Fix this!!
    }

    override fun <F> render(async: Async<F>, name: String, vararg modelAttributes: Any): Kind<F, ServerResponseK> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <F> render(async: Async<F>, name: String, model: Map<String, *>): Kind<F, ServerResponseK> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private inner class BodyInserterResponse() : ServerResponseK {

        override fun statusCode(): HttpStatus {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun headers(): HttpHeaders {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun cookies(): MultiValueMap<String, ResponseCookie> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun <F> writeTo(async: Async<F>, exchange: ServerWebExchange, context: ServerResponse.Context): Kind<F, Unit> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}

