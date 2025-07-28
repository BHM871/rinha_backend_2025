package com.example.redis.core.app.components

import com.example.redis.core.app.Component
import java.math.BigDecimal

interface Queuer : Component {
    fun enqueue(score: BigDecimal, payment: String) : Boolean {
        TODO("Not Implemented Yet")
    }
    fun dequeue(reverse: Boolean = true) : String? {
        TODO("Not Implemented Yet")
    }
}