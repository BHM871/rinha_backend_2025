package com.example.backend.handlers

import com.example.backend.core.Payment
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext

class PostPaymentsHandler : Handler() {
    override suspend fun invoke(ctx: RoutingContext) {
        call = ctx.call
        call.receive<Payment>()
    }
}
