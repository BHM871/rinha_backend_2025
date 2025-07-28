package com.example.api.handlers

import com.example.api.repository.RedisRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.RoutingHandler
import java.math.BigDecimal

class PostPaymentsHandler(
    private val repository: RedisRepository
) : RoutingHandler {
    override suspend fun invoke(ctx: RoutingContext) {
        try {
            val payment = ctx.call.receive<String>()

            while(!this.repository.enqueue(BigDecimal.ZERO, payment))

            ctx.call.respondJson(body = "")
        } catch (_: Exception) {
            ctx.call.respondJson(body = "", status = HttpStatusCode.BadRequest)
        }
    }

    suspend inline fun <reified T> ApplicationCall.respondJson(
        body: T?,
        status: HttpStatusCode = HttpStatusCode.OK
    ) {
        respondNullable(
            status, body
        )
    }
}
