package com.example.backend.handlers

import io.ktor.server.routing.RoutingCall

class GetPaymentsSummaryHandler(override val ctx: RoutingCall) : Handler() {
    override suspend fun process() {
        respond("{\"message\":\"teste\"}")
    }
}
