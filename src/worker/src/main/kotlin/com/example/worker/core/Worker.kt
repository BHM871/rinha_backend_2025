package com.example.worker.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration

class Worker {

    private val log: Logger = LoggerFactory.getLogger(Worker::class.java)

    fun start() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            log.info("Starting Worker...")

            Properties().loadProperties()

            val injectionResolver = Injections(this@Worker)
            injectionResolver.loadInjectors()
            injectionResolver.inject()

            val processors = Processors(this@Worker, Dispatchers.IO)
            processors.loadProcessors()
            processors.startAll()

            log.info("Worker Started.")
            delay(Duration.INFINITE)
        }
    }
}
