package com.example.redis.core.app.components

import com.example.models.core.Payment
import com.example.redis.core.app.Component

interface Enqueuer : Component {
    fun enqueue(payment: Payment)
}