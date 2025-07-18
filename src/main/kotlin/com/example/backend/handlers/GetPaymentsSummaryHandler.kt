package com.example.backend.handlers

import com.example.backend.core.ProcessorInfos
import com.example.backend.core.Summary
import io.ktor.server.routing.RoutingCall
import java.math.BigDecimal

class GetPaymentsSummaryHandler(override val ctx: RoutingCall) : Handler() {
    override suspend fun process() {
        respond(
            Summary(
                default = ProcessorInfos(43236, BigDecimal.valueOf(100000.98)),
                fallback = ProcessorInfos(423545, BigDecimal.valueOf(329347.34))
            )
        )
    }
}
