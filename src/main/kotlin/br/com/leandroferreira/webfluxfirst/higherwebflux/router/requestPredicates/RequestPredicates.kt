package br.com.leandroferreira.webfluxfirst.higherwebflux.router.requestPredicates

import arrow.syntax.function.partially1
import br.com.leandroferreira.webfluxfirst.higherwebflux.core.serverRequest.ServerRequestK
import br.com.leandroferreira.webfluxfirst.higherwebflux.utils.RequestPredicateFn
import org.springframework.http.HttpMethod
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser

private fun httpMethodPredicate(httpMethods: Set<HttpMethod>, requestK: ServerRequestK): Boolean =
    httpMethods.contains(requestK.method())

private fun pathPredicate(pattern: String, requestK: ServerRequestK): Boolean {
    val pathPattern : PathPattern = defaultPatternParser().parse(pattern)
    val info = pathPattern.matchAndExtract(requestK.pathContainer())

    return info != null
}

private fun defaultPatternParser() = PathPatternParser()

private fun RequestPredicateFn.and(second: RequestPredicateFn) : RequestPredicateFn = { requestK ->
    this(requestK) && second(requestK)
}

private val getHttp: RequestPredicateFn = ::httpMethodPredicate.partially1(setOf(HttpMethod.GET))

fun GetK(pattern: String) : RequestPredicateFn = getHttp.and(::pathPredicate.partially1(pattern))
