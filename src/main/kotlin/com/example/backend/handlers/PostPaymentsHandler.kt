package com.example.backend.handlers

import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingCall

class PostPaymentsHandler(override val ctx: RoutingCall) : Handler() {
    override suspend fun process() {
        respond(ctx.receive<String>())
    }
}
