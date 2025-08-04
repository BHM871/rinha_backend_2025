package com.example.redis.core.app.components

import com.example.redis.core.app.Component

interface Queuer : Component {
    fun enqueue(payment: String) : Boolean {
        TODO("Not Implemented Yet")
    }
    fun dequeue() : String? {
        TODO("Not Implemented Yet")
    }
}