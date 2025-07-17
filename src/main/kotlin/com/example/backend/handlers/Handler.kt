package com.example.backend.handlers

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.header
import io.ktor.server.response.respondNullable
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingCall

abstract class Handler {

    abstract val ctx: RoutingCall

    abstract suspend fun process()

    protected fun valid(): Boolean {
        TODO("Not Implemented")
    }

    protected suspend inline fun <reified T> respond(
        body: T? = null,
        contentType: ContentType = ContentType.parse("application/json; charset=UTF-8"),
        status: HttpStatusCode = HttpStatusCode.OK
    ) {
        ctx.response.header("Content-Type", contentType.contentType)
        ctx.respondNullable(status, body)
    }
}