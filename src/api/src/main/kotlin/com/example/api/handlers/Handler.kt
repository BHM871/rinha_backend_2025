package com.example.api.handlers

import com.example.api.repository.RedisRepository
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.header
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.RoutingHandler

abstract class Handler : RoutingHandler {

    abstract val repository: RedisRepository
    lateinit var call: RoutingCall

    protected open fun isValid(value: Any): Boolean {
        TODO("Not Implemented")
    }

    protected suspend inline fun <reified T> respond(
        body: T? = null,
        contentType: ContentType = ContentType.parse("application/json; charset=UTF-8"),
        status: HttpStatusCode = HttpStatusCode.OK
    ) {
        call.response.header("Content-Type", "${contentType.contentType}/${contentType.contentSubtype}")
        call.respondNullable(status, body)
    }
}