package com.example.worker.app.config

import com.example.redis.InMemoryMediator
import com.example.redis.RedisMediator
import com.example.redis.core.app.Mediator
import com.example.worker.app.models.DefaultProcessor
import com.example.worker.app.models.FallbackProcessor
import com.example.worker.client.ClientGateway
import com.example.worker.core.Worker
import com.example.worker.core.dependencies
import com.example.worker.core.property
import com.example.worker.processors.HealthProcessor
import com.example.worker.processors.PaymentProcessor
import com.example.worker.repository.Repository

fun Worker.inject() {
    databaseInject()
    gatewayInject()
    processorInject()
}

// =================================================

private fun Worker.databaseInject() {
//    redisInject()
    inMemoryInject()

    val repository = Repository( dependencies.resolve(Mediator::class)!! )
    dependencies.provide(Repository::class) { repository }
}

private fun Worker.redisInject() {
    val host = property("redis.host", String::class)!!
    val port = property("redis.port", Int::class)!!
    val poolSize = property("redis.poolSize", Int::class)

    val mediator = RedisMediator(host, port, poolSize)
    dependencies.provide(Mediator::class) { mediator }
}

private fun Worker.inMemoryInject() {
    val mediator = InMemoryMediator()
    dependencies.provide(Mediator::class) { mediator }
}

// =================================================

private fun Worker.gatewayInject() {
    var host = property("gateway.default.host", String::class)!!
    var port = property("gateway.default.port", Int::class)!!
    var client = ClientGateway(host, port)

    val default = DefaultProcessor(client)
    dependencies.provide(DefaultProcessor::class) { default }

    host = property("gateway.fallback.host", String::class)!!
    port = property("gateway.fallback.port", Int::class)!!
    client = ClientGateway(host, port)
    val fallback = FallbackProcessor( client )

    dependencies.provide(FallbackProcessor::class) { fallback }
}

// =================================================

private fun Worker.processorInject() {
    val payment = PaymentProcessor(
        dependencies.resolve(Repository::class)!!,
        dependencies.resolve(DefaultProcessor::class)!!,
        dependencies.resolve(FallbackProcessor::class)!!
    )
    val health = HealthProcessor(
        dependencies.resolve(DefaultProcessor::class)!!,
        dependencies.resolve(FallbackProcessor::class)!!
    )
    val consumer = CacheProcessor(
        dependencies.resolve(Repository::class)!!,
        property("worker.processors.cache.batch", Int::class) ?: 10
    )

    dependencies.provide(PaymentProcessor::class) { payment }
    dependencies.provide(HealthProcessor::class) { health }
    dependencies.provide(CacheProcessor::class) { consumer }
}