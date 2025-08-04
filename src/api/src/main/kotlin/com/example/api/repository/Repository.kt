package com.example.api.repository

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.models.core.Summary
import com.example.database.core.app.Event
import com.example.database.core.app.Mediator
import com.example.database.core.app.components.Queuer
import com.example.database.core.app.components.Storage

class Repository(
    private val mediator: Mediator
) {

    fun enqueue(payment: Payment) : Boolean {
        return queue.enqueue(payment)
    }

    fun getSummary(filter: FilterSummary) : Summary {
        return storer.getSummary(filter)
    }

    val queue = object : Queuer {
        override val mediator: Mediator
            get() = this@Repository.mediator

        override fun enqueue(payment: Payment) : Boolean {
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
            get() = this@Repository.mediator

        override fun getSummary(filter: FilterSummary): Summary {
            return (this.mediator.notify(this, Event.SUMMARY, filter) as Summary?)!!
        }
    }
}
