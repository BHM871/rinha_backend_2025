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
        try {
            var tmp = default.client.health()
            defaultHealth = Health(
                tmp.failing,
                if (tmp.minResponseTime <= 0) 1_000_000 else tmp.minResponseTime
            )
            tmp = fallback.client.health()
            fallbackHealth = Health(
                tmp.failing,
                if (tmp.minResponseTime <= 0) 1_000_000 else tmp.minResponseTime
            )
        } catch (_: Exception) {
        }
        delay(5000)
    }
}