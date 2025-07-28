package com.example.worker.processors

import com.example.worker.app.models.Health
import com.example.worker.client.DefaultGateway
import com.example.worker.client.FallbackGateway
import com.example.worker.core.pool.Processor
import kotlinx.coroutines.delay

class HealthProcessor(
    private val defaultGateway: DefaultGateway,
    private val fallbackGateway: FallbackGateway
) : Processor {

    companion object {
        var defaultHealth: Health? = null
        var fallbackHealth: Health? = null
    }

    override suspend fun process() {
        while(true) {
            try {
                defaultHealth = defaultGateway.health()
                fallbackHealth = fallbackGateway.health()
            } catch (_: Exception) {
            }
            delay(5000)
        }
    }
}