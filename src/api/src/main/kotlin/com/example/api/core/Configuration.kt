package com.example.api.core

import com.example.api.repository.Repository
import com.example.database.InMemoryMediator
import com.example.database.RedisMediator
import com.example.database.core.app.Mediator
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies

suspend fun Application.configureDatabase() {
//    configureRedis()
    configureInMemory()

    val repository = Repository(dependencies.resolve<Mediator>())
    dependencies.provide<Repository> { repository }
}

private fun Application.configureRedis() {
    val host = property<String>("redis.host")
    val port = property<Int>("redis.port")
    val poolSize = property<Int>("redis.poolSize")

    val mediator = RedisMediator(host, port, poolSize)
    dependencies.provide<Mediator> { mediator }
}

private fun Application.configureInMemory() {
    val mediator = InMemoryMediator()
    dependencies.provide<Mediator> { mediator }
}