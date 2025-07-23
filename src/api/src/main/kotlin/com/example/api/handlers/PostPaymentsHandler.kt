package com.example.api.handlers

import com.example.api.repository.RedisRepository
import com.example.models.core.Payment
import com.example.models.core.now
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.RoutingContext
import java.util.UUID

class PostPaymentsHandler(
    override val repository: RedisRepository
) : Handler() {
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

        this.repository.enqueue(payment)
        respond<String>()
    }

    override fun isValid(value: Any): Boolean {
        return if (value !is Payment) false
        else UUID.fromString(value.correlationId) != null
    }
}
