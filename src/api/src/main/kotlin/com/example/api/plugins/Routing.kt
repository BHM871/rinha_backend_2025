package com.example.api.plugins

import com.example.api.handlers.PostPaymentsHandler
import com.example.api.handlers.GetPaymentsSummaryHandler
import com.example.api.repository.RedisRepository
import com.example.redis.core.app.Mediator
import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting(repository: RedisRepository) {
    routing {
        post("/payments"        , PostPaymentsHandler(repository)      )
        get ("/payments-summary", GetPaymentsSummaryHandler(repository))
    }
}
