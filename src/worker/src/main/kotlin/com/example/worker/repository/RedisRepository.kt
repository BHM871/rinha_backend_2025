package com.example.worker.repository

import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal
import java.time.LocalDateTime

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

    override fun store(store: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        this.mediator.notify(this, Event.STORAGE, store, date, isDefault)
    }
}