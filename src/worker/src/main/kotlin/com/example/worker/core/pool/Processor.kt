package com.example.worker.core.pool

interface Processor {
    suspend fun process()
}