package com.example.backend.handlers

import com.example.backend.core.FilterSummary
import com.example.backend.core.ProcessorInfos
import com.example.backend.core.Summary
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.RoutingContext
import java.math.BigDecimal

class GetPaymentsSummaryHandler : Handler() {

    override suspend fun invoke(ctx: RoutingContext) {
        this.call = ctx.call

        val parameters = this.call.parameters
        val filter = FilterSummary(
            parameters["from"],
            parameters["to"]
        )

        if (!isValid(filter)) respond<String>(status = HttpStatusCode.UnprocessableEntity)

        respond(
            Summary(
                default = ProcessorInfos(43236, BigDecimal.valueOf(100000.98)),
                fallback = ProcessorInfos(423545, BigDecimal.valueOf(329347.34))
            )
        )
    }
}
