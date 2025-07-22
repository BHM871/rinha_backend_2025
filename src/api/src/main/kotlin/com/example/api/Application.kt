package com.example.api

import com.example.api.core.configureRedis
import com.example.api.plugins.configureRouting
import com.example.api.plugins.configureSerialization
import com.example.redis.core.app.Mediator
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.di.dependencies

fun main(args: Array<String>) {
    EngineMain.main(args)
}

suspend fun Application.module() {
    configureRedis()
    configureSerialization()
    configureRouting(dependencies.resolve<Mediator>())
}
