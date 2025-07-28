package com.example.worker.app.interfaces

import com.example.worker.app.models.Health

interface Gateway {
    val host: String
    val port: Int
    suspend fun processor(payment: String) : Boolean
    suspend fun health(): Health
}