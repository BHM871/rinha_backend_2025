package com.example.api.handlers

import com.example.api.core.Payment
import com.example.api.core.now
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext
import java.util.UUID

class PostPaymentsHandler : Handler() {
    override suspend fun invoke(ctx: RoutingContext) {
        call = ctx.call

        var payment = call.receive<Payment>()
        if (!isValid(payment)) {
            respond<String>(status = HttpStatusCode.UnprocessableEntity)
            return
        }

        payment = Payment(
            payment.correlationId,
            payment.amount,
            now()
        )
    }

    override fun isValid(value: Any): Boolean {
        return if (value !is Payment) false
        else UUID.fromString(value.correlationId) != null
    }
}
