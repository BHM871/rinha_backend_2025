package com.example.api.handlers

import com.example.models.core.FilterSummary
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Summarier
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.RoutingContext
import java.math.BigDecimal

class GetPaymentsSummaryHandler(
    override val mediator: Mediator
) : Handler(), Summarier {

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

        respond(this.getSummary(filter))
    }

    override fun isValid(value: Any): Boolean {
        return if (value !is FilterSummary) false
        else !(value.from != null && value.to != null && value.to!! < value.from!!)
    }

    override fun getSummary(filter: FilterSummary): Summary {
        return (this.mediator.notify(this, filter)
            ?: Summary(
                ProcessorInfos(0, BigDecimal.valueOf(0)),
                ProcessorInfos(0, BigDecimal.valueOf(0))
            )) as Summary
    }
}
