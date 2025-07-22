package com.example.api

import com.example.api.core.configureRedis
import com.example.api.plugins.configureRouting
import com.example.api.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

suspend fun Application.module() {
    configureRedis()
    configureSerialization()
    configureRouting()
}
