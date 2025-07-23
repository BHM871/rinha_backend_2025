package com.example.api.core

import com.example.api.repository.RedisRepository
import com.example.redis.RedisMediator
import com.example.redis.core.app.Mediator
import com.example.redis.gateway.RedisQueue
import com.example.redis.gateway.RedisStorage
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies

fun Application.configureRedis() {
    val host = property<String>("redis.host")
    val port = property<Int>("redis.port")
    dependencies.provide<Mediator> { RedisMediator(host, port) }
    dependencies.provide<RedisRepository> { RedisRepository(dependencies.resolve<Mediator>()) }
}