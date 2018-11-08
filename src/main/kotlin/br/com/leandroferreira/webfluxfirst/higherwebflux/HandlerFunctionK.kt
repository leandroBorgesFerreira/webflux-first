package br.com.leandroferreira.webfluxfirst.higherwebflux

import arrow.Kind
import arrow.effects.typeclasses.Async
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

interface HandlerFunctionK<T : ServerResponse> {

    /**
     * Handle the given request.
     * @param request the request to handle
     * @return the response
     */
    fun <F> handle(async: Async<F>, request: ServerRequest): Kind<F, T>

}
