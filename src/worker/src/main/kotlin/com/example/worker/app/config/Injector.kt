package com.example.worker.app.config

import com.example.redis.RedisMediator
import com.example.redis.core.app.Mediator
import com.example.worker.core.Application
import com.example.worker.core.dependencies
import com.example.worker.core.property
import com.example.worker.processors.PaymentProcessor
import com.example.worker.repository.RedisRepository

fun Application.inject() {
    redisInject()
    processorInject()
}

fun Application.redisInject() {
    val host = property("redis.host", String::class)!!
    val port = property("redis.port", Int::class)!!

    dependencies.provide(Mediator::class) { RedisMediator(host, port) }
    dependencies.provide(RedisRepository::class) { RedisRepository(
        dependencies.resolve(Mediator::class)!!
    ) }
}

fun Application.processorInject() {
    dependencies.provide(PaymentProcessor::class) { PaymentProcessor(
        dependencies.resolve(RedisRepository::class)!!
    ) }
}