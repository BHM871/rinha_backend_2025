package com.example.redis

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.redis.core.app.Component
import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import com.example.redis.gateway.RedisQueue
import com.example.redis.gateway.RedisStorage

class RedisMediator : Mediator {
    
    private val queue: RedisQueue
    private val storage: RedisStorage

    constructor(host: String, port: Int) {
        queue = RedisQueue(host, port)
        storage = RedisStorage(host, port)
    }

    override fun notify(component: Component, event: Event, data: Any?) : Any? {
        return when (component) {
            is Queuer -> when (event) {
                Event.ENQUEUE -> queue.enqueue(data!! as Payment)
                Event.DEQUEUE -> queue.dequeue(data!! as Boolean)
                else -> null
            }
            is Storage -> when(event) {
                Event.STORAGE -> storage.store(data!! as Payment)
                Event.SUMMARY -> storage.getSummary(data!! as FilterSummary)
                else -> null
            }
            else -> null
        }
    }
}