package com.example.api.handlers

import com.example.api.repository.RedisRepository
import com.example.models.core.FilterSummary
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.RoutingContext

class GetPaymentsSummaryHandler(
    override val repository: RedisRepository
) : Handler() {

    override suspend fun invoke(ctx: RoutingContext) {
        this.call = ctx.call

        val parameters = this.call.parameters
        val filter = FilterSummary(
            parameters["from"],
            parameters["to"]
        )

        if (!isValid(filter)) {
            return respond<String>(status = HttpStatusCode.UnprocessableEntity)
        }

        respond(this.repository.getSummary(filter))
    }

    override fun isValid(value: Any): Boolean {
        return if (value !is FilterSummary) false
        else !(value.from != null && value.to != null && value.to!! < value.from!!)
    }
}
