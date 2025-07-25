package com.example.worker.repository

import com.example.models.core.Payment
import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage

class RedisRepository(
    override val mediator: Mediator
) : Queuer, Storage {
    override fun dequeue(reverse: Boolean): Payment? {
        return this.mediator.notify(this, Event.DEQUEUE, reverse) as Payment?
    }
}