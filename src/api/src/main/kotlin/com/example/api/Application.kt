package com.example.api

import com.example.api.core.configureDatabase
import com.example.api.plugins.configureRouting
import com.example.api.plugins.configureSerialization
import com.example.api.repository.Repository
import com.example.worker.core.Worker
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.di.dependencies

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        module()
        worker()
    }.start(wait = true)
}

suspend fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureRouting(dependencies.resolve<Repository>())
}

fun Application.worker() {
    Worker().start()
}
