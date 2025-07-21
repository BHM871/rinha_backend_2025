package com.example.api.handlers

import com.example.api.core.Payment
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext
import java.util.UUID

class PostPaymentsHandler : Handler() {
    override suspend fun invoke(ctx: RoutingContext) {
        call = ctx.call

        val payment = call.receive<Payment>()
        if (!isValid(payment)) {
            respond<String>(status = HttpStatusCode.UnprocessableEntity)
            return
        }

    }

    override fun isValid(value: Any): Boolean {
        return if (value !is Payment) false
        else UUID.fromString(value.correlationId) != null
    }
}
