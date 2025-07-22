package com.example.api.handlers

import com.example.models.core.Payment
import com.example.models.core.now
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Enqueuer
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext
import java.util.UUID

class PostPaymentsHandler(
    override val mediator: Mediator
) : Handler(), Enqueuer {
    override suspend fun invoke(ctx: RoutingContext) {
        call = ctx.call

        var payment = call.receive<Payment>()
        if (!isValid(payment)) {
            return respond<String>(status = HttpStatusCode.UnprocessableEntity)
        }

        payment = Payment(
            payment.correlationId,
            payment.amount,
            now()
        )

        this.enqueue(payment)
        respond<String>()
    }

    override fun isValid(value: Any): Boolean {
        return if (value !is Payment) false
        else UUID.fromString(value.correlationId) != null
    }

    override fun enqueue(payment: Payment) {
        this.mediator.notify(this, payment)
    }
}
