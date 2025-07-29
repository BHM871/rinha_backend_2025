package com.example.worker.processors

import com.example.models.core.Payment
import com.example.worker.app.models.DefaultProcessor
import com.example.worker.app.models.FallbackProcessor
import com.example.worker.app.models.Health
import com.example.worker.core.pool.Processor
import com.example.worker.repository.RedisRepository

class PaymentProcessor(
    private val repository: RedisRepository,
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

            val payment = repository.dequeue(onTop(defaultHealth, fallbackHealth))
            if (payment == null)
                return

            val success = if (onTop(defaultHealth, fallbackHealth)) {
                default.client.processor(payment, (defaultHealth.minResponseTime * 1.5).toLong())
            } else {
                fallback.client.processor(payment, (defaultHealth.minResponseTime * 1.5).toLong())
            }

            if (success == null) return

            val score = Payment.getScore(payment)
            val date = Payment.getDate(payment)

            if (success) repository.store(score, date, onTop(defaultHealth, fallbackHealth))
            else repository.enqueue(Payment.getScore(payment), payment)
        } catch (_: Exception) {
        }
    }

    private fun onTop(default: Health, fallback: Health) : Boolean {
        return (!default.failing && !fallback.failing && default.minResponseTime <= fallback.minResponseTime) || !default.failing
    }
}
