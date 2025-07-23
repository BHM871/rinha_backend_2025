package com.example.worker.application

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Application {

    private val log: Logger = LoggerFactory.getLogger(Application::class.java)

    fun start() {
        log.info("Starting Worker...")
        Properties().loadProperties()
        log.info("Worker Started.")
    }
}