package com.example.worker.processors

import com.example.worker.client.DefaultGateway
import com.example.worker.client.FallbackGateway
import com.example.worker.core.pool.Processor
import com.example.worker.repository.RedisRepository

class PaymentProcessor(
    private val repository: RedisRepository,
    private val default: DefaultGateway,
    private val fallback: FallbackGateway
) : Processor {
    override suspend fun process() {
        val payment = repository.dequeue()
        if (payment == null)
            return

        println(payment)

        if (!default.processor(payment))
        else fallback.processor(payment)
    }
}
