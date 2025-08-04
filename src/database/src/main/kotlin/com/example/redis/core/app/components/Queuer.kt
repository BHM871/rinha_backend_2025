package com.example.redis.core.app.components

import com.example.models.core.Payment
import com.example.redis.core.app.Component

interface Queuer : Component {
    fun enqueue(payment: Payment) : Boolean {
        TODO("Not Implemented Yet")
    }
    fun dequeue() : Payment? {
        TODO("Not Implemented Yet")
    }
}