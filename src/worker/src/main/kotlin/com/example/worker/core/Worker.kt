package com.example.worker.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration

class Worker {

    private val log: Logger = LoggerFactory.getLogger(Worker::class.java)

    suspend fun start() = coroutineScope {
        log.info("Starting Worker...")

        Properties().loadProperties()

        val injectionResolver = Injections(this@Worker)
        injectionResolver.loadInjectors()
        injectionResolver.inject()

        val processors = Processors(this@Worker, Dispatchers.Default)
        processors.loadProcessors()
        processors.startAll()

        log.info("Worker Started.")
        delay(Duration.INFINITE)
    }
}
