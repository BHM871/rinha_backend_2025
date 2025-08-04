package com.example.worker.processors

import com.example.worker.app.models.DefaultProcessor
import com.example.worker.app.models.FallbackProcessor
import com.example.worker.app.models.Health
import com.example.worker.core.pool.Processor
import com.example.worker.repository.Repository

class PaymentProcessor(
    private val repository: Repository,
    private val default: DefaultProcessor,
    private val fallback: FallbackProcessor
) : Processor {
    override suspend fun process() {
        try {
            val defaultHealth = HealthProcessor.defaultHealth
            val fallbackHealth = HealthProcessor.fallbackHealth

            if (defaultHealth == null || fallbackHealth == null)
                return
            if (defaultHealth.failing && fallbackHealth.failing)
                return

            val onTop = onTop(defaultHealth, fallbackHealth)
            val payment = CacheProcessor.dequeue(onTop)
            if (payment == null)
                return

            val success = if (onTop) {
                default.client.processor(payment.body, (defaultHealth.minResponseTime * 1.5).toLong())
            } else {
                fallback.client.processor(payment.body, (defaultHealth.minResponseTime * 1.5).toLong())
            }

            if (success == null) return

            if (success) repository.store(payment.score, payment.date, onTop)
            else CacheProcessor.enqueue(payment)
        } catch (_: Exception) {
        }
    }

    private fun onTop(default: Health, fallback: Health) : Boolean {
        return (!default.failing && !fallback.failing && default.minResponseTime <= fallback.minResponseTime) || !default.failing
    }
}
