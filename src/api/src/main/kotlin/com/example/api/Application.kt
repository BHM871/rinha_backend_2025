package com.example.api

import com.example.api.core.configureDatabase
import com.example.api.plugins.configureRouting
import com.example.api.plugins.configureSerialization
import com.example.api.repository.Repository
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.di.dependencies

fun main(args: Array<String>) {
    EngineMain.main(args)
}

suspend fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureRouting(dependencies.resolve<Repository>())
}
