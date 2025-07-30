package com.example.api.repository

import com.example.models.core.FilterSummary
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal

class RedisRepository(
    private val mediator: Mediator
) {

    fun enqueue(payment: String) : Boolean {
        return queue.enqueue(payment)
    }

    fun getSummary(filter: FilterSummary) : Summary {
        return storer.getSummary(filter)
    }

    val queue = object : Queuer {
        override val mediator: Mediator
            get() = this@RedisRepository.mediator

        override fun enqueue(payment: String) : Boolean {
            return try {
                this.mediator.notify(this, Event.ENQUEUE, payment)
                true
            } catch (_: Exception) {
                false
            }
        }
    }

    val storer = object : Storage {
        override val mediator: Mediator
            get() = this@RedisRepository.mediator

        override fun getSummary(filter: FilterSummary): Summary {
            return (this.mediator.notify(this, Event.SUMMARY, filter) as Summary?)!!
        }
    }
}
