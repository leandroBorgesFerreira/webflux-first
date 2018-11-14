package br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest

import org.springframework.http.HttpCookie
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.server.ServerWebExchange
import java.net.InetSocketAddress
import java.net.URI
import java.util.*

class DefaultServerRequestK(exchange: ServerWebExchange, messageReaders: List<HttpMessageReader<*>>) : ServerRequestK {
    override fun methodName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uri(): URI {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun headers(): ServerRequestK.Headers {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cookies(): MultiValueMap<String, HttpCookie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remoteAddress(): Optional<InetSocketAddress> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun messageReaders(): List<HttpMessageReader<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> body(extractor: BodyExtractor<T, in ServerHttpRequest>): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> body(extractor: BodyExtractor<T, in ServerHttpRequest>, hints: Map<String, Any>): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}