package com.example.worker.processors

import com.example.worker.app.models.DefaultProcessor
import com.example.worker.app.models.FallbackProcessor
import com.example.worker.app.models.Health
import com.example.worker.core.pool.Processor
import kotlinx.coroutines.delay

class HealthProcessor(
    private val default: DefaultProcessor,
    private val fallback: FallbackProcessor
) : Processor {

    companion object {
        var defaultHealth: Health? = null
        var fallbackHealth: Health? = null
    }

    override suspend fun process() {
        while(true) {
            try {
                defaultHealth = default.client.health()
                fallbackHealth = fallback.client.health()
            } catch (_: Exception) {
            }
            delay(5000)
        }
    }
}