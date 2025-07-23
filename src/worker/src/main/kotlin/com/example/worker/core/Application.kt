package com.example.worker.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class Application {

    private val log: Logger = LoggerFactory.getLogger(Application::class.java)

    fun start() {
        log.info("Starting Worker...")
        Properties().loadProperties()
        log.info("Worker Started.")
    }
}

fun <T : Any> Application.property(key: String, type: KClass<T>) : T? {
    return Properties.property<T>(key, type)
}
