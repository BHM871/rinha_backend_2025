package com.example.redis.gateway

import com.example.models.core.Payment
import com.example.redis.core.RedisClient
import redis.clients.jedis.ConnectionPoolConfig
import redis.clients.jedis.JedisPooled

class RedisQueue(
    override val host: String,
    override val port: Int,
    override val poolConfig: ConnectionPoolConfig
) : RedisClient {

    companion object {
        private lateinit var jedis: JedisPooled
        private var isSetup = false
    }

    private val queue = "payments:queue"

    override fun setup() {
        if (!isSetup) {
            jedis = JedisPooled(poolConfig, host, port)
            isSetup = true
        }
    }

    fun enqueue(payment: Payment) {
        if (!isSetup)
            setup()

        jedis.lpush(
            queue,
            payment.body
        )
    }

     fun dequeue(): Payment? {
        if (!isSetup)
            setup()

         return Payment(jedis.rpop(queue))
    }
}
