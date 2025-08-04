package com.example.worker.repository

import com.example.models.core.Payment
import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal
import java.time.LocalDateTime

class Repository(
    private val mediator: Mediator
) {

    fun dequeue(onTop: Boolean = true) : Payment? {
        return queue.dequeue(onTop)
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        storer.store(score, date, isDefault)
    }

    val queue = object : Queuer {
        override val mediator: Mediator
            get() = this@Repository.mediator

        override fun dequeue(onTop: Boolean) : Payment? {
            return this.mediator.notify(this, Event.DEQUEUE, onTop) as Payment?
        }
    }

    val storer = object : Storage {
        override val mediator: Mediator
            get() = this@Repository.mediator

        override fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
            this.mediator.notify(this, Event.STORAGE, score, date, isDefault)
        }
    }
}