package com.example.worker.repository

import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal
import java.time.LocalDateTime

class RedisRepository(
    private val mediator: Mediator
) {

    fun enqueue(score: BigDecimal, payment: String) {
        queue.enqueue(score, payment)
    }

    fun dequeue(onTop: Boolean = true) : String? {
        return queue.dequeue(onTop)
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        storer.store(score, date, isDefault)
    }

    val queue = object : Queuer {
        override val mediator: Mediator
            get() = this@RedisRepository.mediator

        override fun enqueue(score: BigDecimal, payment: String) : Boolean {
            return try {
                this.mediator.notify(this, Event.ENQUEUE, score, payment)
                true
            } catch (_: Exception) {
                false
            }
        }

        override fun dequeue(reverse: Boolean) : String? {
            return this.mediator.notify(this, Event.DEQUEUE, reverse) as String?
        }
    }

    val storer = object : Storage {
        override val mediator: Mediator
            get() = this@RedisRepository.mediator

        override fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
            this.mediator.notify(this, Event.STORAGE, score, date, isDefault)
        }
    }
}