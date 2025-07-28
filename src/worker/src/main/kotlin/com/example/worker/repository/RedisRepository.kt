package com.example.worker.repository

import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal

class RedisRepository(
    override val mediator: Mediator
) : Queuer, Storage {
    override fun enqueue(score: BigDecimal, payment: String): Boolean {
        return try {
            this.mediator.notify(this, Event.ENQUEUE, score, payment)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun dequeue(reverse: Boolean): String? {
        return this.mediator.notify(this, Event.DEQUEUE, reverse) as String?
    }
}