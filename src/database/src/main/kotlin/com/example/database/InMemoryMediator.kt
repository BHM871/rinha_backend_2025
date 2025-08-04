package com.example.database

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.database.core.app.Component
import com.example.database.core.app.Event
import com.example.database.core.app.Mediator
import com.example.database.core.app.components.Queuer
import com.example.database.core.app.components.Storage
import com.example.database.middleware.InMemoryQueue
import com.example.database.middleware.InMemoryStorage
import java.math.BigDecimal
import java.time.LocalDateTime

class InMemoryMediator : Mediator {

    private val queue = InMemoryQueue()
    private val storage = InMemoryStorage()

    override fun notify(component: Component, event: Event, vararg data: Any?) : Any? {
        return when (component) {
            is Queuer -> when (event) {
                Event.ENQUEUE -> queue.enqueue(data[0]!! as Payment)
                Event.DEQUEUE -> queue.dequeue(data[0] as Boolean? ?: false)
                else -> null
            }
            is Storage -> when(event) {
                Event.STORAGE -> storage.store(data[0]!! as BigDecimal, data[1]!! as LocalDateTime, data[2]!! as Boolean)
                Event.SUMMARY -> storage.getSummary(data[0]!! as FilterSummary)
                else -> null
            }
            else -> null
        }
    }
}