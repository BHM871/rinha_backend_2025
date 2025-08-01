package com.example.api.core

import com.example.api.repository.RedisRepository
import com.example.redis.RedisMediator
import com.example.redis.core.app.Mediator
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies

suspend fun Application.configureRedis() {
    val host = property<String>("redis.host")
    val port = property<Int>("redis.port")
    val poolSize = property<Int>("redis.poolSize")

    val mediator = RedisMediator(host, port, poolSize)
    dependencies.provide<Mediator> { mediator }

    val repository = RedisRepository(dependencies.resolve<Mediator>())
    dependencies.provide<RedisRepository> { repository }
}