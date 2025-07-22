package com.example.redis.core.app.components

import com.example.models.core.Payment
import com.example.redis.core.app.Component

interface Dequeuer : Component {
    fun dequeue() : Payment?
}