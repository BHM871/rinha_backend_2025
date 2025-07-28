package com.example.worker.processors

import com.example.models.core.Payment
import com.example.worker.app.models.Health
import com.example.worker.client.DefaultGateway
import com.example.worker.client.FallbackGateway
import com.example.worker.core.pool.Processor
import com.example.worker.repository.RedisRepository

class PaymentProcessor(
    private val repository: RedisRepository,
    private val defaultGateway: DefaultGateway,
    private val fallbackGateway: FallbackGateway
) : Processor {
    override suspend fun process() {
        val defaultHealth = HealthProcessor.defaultHealth
        val fallbackHealth = HealthProcessor.fallbackHealth

        if (defaultHealth == null || fallbackHealth == null)
            return
        if (defaultHealth.failing && fallbackHealth.failing)
            return

        val payment = repository.dequeue(onTop(defaultHealth, fallbackHealth))
        if (payment == null)
            return

        val success = if (onTop(defaultHealth, fallbackHealth)) {
            defaultGateway.processor(payment)
        } else {
            fallbackGateway.processor(payment)
        }

        if (!success) {
            repository.enqueue(Payment.getScore(payment), payment)
        }
    }

    private fun onTop(default: Health, fallback: Health) : Boolean {
        return (!default.failing && !fallback.failing && default.minResponseTime <= fallback.minResponseTime) || !default.failing
    }
}
