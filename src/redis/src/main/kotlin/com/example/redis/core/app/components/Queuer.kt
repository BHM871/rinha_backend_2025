package com.example.redis.core.app.components

import com.example.models.core.Payment
import com.example.redis.core.app.Component

interface Queuer : Component {
    fun enqueue(payment: Payment) {
        TODO("Not Implemented Yet")
    }
    fun dequeue(reverse: Boolean = true) : Payment? {
        TODO("Not Implemented Yet")
    }
}