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

    fun dequeue() : String? {
        return queue.dequeue()
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        storer.store(score, date, isDefault)
    }

    val queue = object : Queuer {
        override val mediator: Mediator
            get() = this@RedisRepository.mediator

        override fun dequeue() : String? {
            return this.mediator.notify(this, Event.DEQUEUE) as String?
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