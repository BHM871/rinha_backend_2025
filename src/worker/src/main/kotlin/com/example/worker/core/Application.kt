package com.example.worker.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration

class Application {

    private val log: Logger = LoggerFactory.getLogger(Application::class.java)

    suspend fun start() = coroutineScope {
        log.info("Starting Worker...")

        Properties().loadProperties()

        log.info("Worker Started.")
        delay(Duration.INFINITE)
    }
}
