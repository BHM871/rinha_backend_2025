package com.example.backend.plugins

import com.example.backend.handlers.PostPaymentsHandler
import com.example.backend.handlers.GetPaymentsSummaryHandler
import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        post("/payments")         { PostPaymentsHandler      (call).process() }
        get ("/payments-summary") { GetPaymentsSummaryHandler(call).process() }
    }
}
