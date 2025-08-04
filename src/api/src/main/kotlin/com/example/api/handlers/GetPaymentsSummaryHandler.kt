package com.example.api.handlers

import com.example.api.repository.Repository
import com.example.models.core.FilterSummary
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondNullable
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.RoutingHandler

class GetPaymentsSummaryHandler(
    private val repository: Repository
) : RoutingHandler {

    override suspend fun invoke(ctx: RoutingContext) {
        try {
            val parameters = ctx.call.parameters
            val filter = FilterSummary(
                parameters["from"],
                parameters["to"]
            )

            ctx.call.respondJson(body = repository.getSummary(filter))
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
