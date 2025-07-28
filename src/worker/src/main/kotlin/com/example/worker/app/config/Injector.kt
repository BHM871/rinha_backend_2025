package com.example.worker.app.config

import com.example.redis.RedisMediator
import com.example.redis.core.app.Mediator
import com.example.worker.client.DefaultGateway
import com.example.worker.client.FallbackGateway
import com.example.worker.core.Worker
import com.example.worker.core.dependencies
import com.example.worker.core.property
import com.example.worker.processors.HealthProcessor
import com.example.worker.processors.PaymentProcessor
import com.example.worker.repository.RedisRepository

fun Worker.inject() {
    redisInject()
    gatewayInject()
    processorInject()
}

fun Worker.redisInject() {
    val host = property("redis.host", String::class)!!
    val port = property("redis.port", Int::class)!!
    val poolSize = property("redis.poolSize", Int::class)

    val mediator = RedisMediator(host, port, poolSize)
    dependencies.provide(Mediator::class) { mediator }

    val repository = RedisRepository( dependencies.resolve(Mediator::class)!! )
    dependencies.provide(RedisRepository::class) { repository }
}

fun Worker.gatewayInject() {
    var host = property("gateway.default.host", String::class)!!
    var port = property("gateway.default.port", Int::class)!!
    val default = DefaultGateway( host, port )

    dependencies.provide(DefaultGateway::class) { default }

    host = property("gateway.fallback.host", String::class)!!
    port = property("gateway.fallback.port", Int::class)!!
    val fallback = FallbackGateway( host, port )

    dependencies.provide(FallbackGateway::class) { fallback }
}

fun Worker.processorInject() {
    val payment = PaymentProcessor(
        dependencies.resolve(RedisRepository::class)!!,
        dependencies.resolve(DefaultGateway::class)!!,
        dependencies.resolve(FallbackGateway::class)!!
    )
    val health = HealthProcessor(
        dependencies.resolve(DefaultGateway::class)!!,
        dependencies.resolve(FallbackGateway::class)!!
    )

    dependencies.provide(PaymentProcessor::class) { payment }
    dependencies.provide(HealthProcessor::class) { health }
}