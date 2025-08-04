package com.example.database.core.app.components

import com.example.models.core.Payment
import com.example.database.core.app.Component

interface Queuer : Component {
    fun enqueue(payment: Payment) : Boolean {
        TODO("Not Implemented Yet")
    }
    fun dequeue(onTop: Boolean) : Payment? {
        TODO("Not Implemented Yet")
    }
}