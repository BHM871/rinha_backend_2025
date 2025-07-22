package com.example.redis

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.redis.core.app.Component
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Dequeuer
import com.example.redis.core.app.components.Enqueuer
import com.example.redis.core.app.components.Storager
import com.example.redis.core.app.components.Summarier
import com.example.redis.gateway.RedisQueue
import com.example.redis.gateway.RedisStorage

class RedisMediator : Mediator {
    
    private val queue: RedisQueue
    private val storage: RedisStorage

    constructor(host: String, port: Int) {
        queue = RedisQueue(host, port)
        storage = RedisStorage(host, port)
    }

    override fun notify(component: Component, data: Any?) : Any? {
        return when (component) {
            is Enqueuer -> queue.enqueue(data!! as Payment)
            is Dequeuer -> queue.dequeue(data!! as Boolean)
            is Storager -> storage.store(data!! as Payment)
            is Summarier -> storage.getSummary(data!! as FilterSummary)
            else -> null
        }
    }
}