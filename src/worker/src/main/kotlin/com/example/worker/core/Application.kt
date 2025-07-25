package com.example.worker.core

import kotlinx.coroutines.Dispatchers
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

        val injectionResolver = Injections(this@Application)
        injectionResolver.loadInjectors()
        injectionResolver.inject()

        val processors = Processors(this@Application, Dispatchers.Default)
        processors.loadProcessors()
        processors.startAll()

        log.info("Worker Started.")
        delay(Duration.INFINITE)
    }
}
