package com.example.worker.processors

import com.example.worker.core.pool.Processor
import com.example.worker.repository.RedisRepository

class PaymentProcessor(
    private val repository: RedisRepository
) : Processor {
    override suspend fun process() {
        val payment = repository.dequeue()
        if (payment == null)
            return

        println(payment)
        // TODO: Process payment
    }
}
