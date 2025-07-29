package com.example.redis.gateway

import com.example.models.core.Payment
import com.example.models.plugins.getSerializer
import com.example.redis.core.RedisClient
import redis.clients.jedis.ConnectionPoolConfig
import redis.clients.jedis.JedisPooled
import java.math.BigDecimal

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

    fun enqueue(store: BigDecimal, payment: String) {
        if (!isSetup)
            setup()

        jedis.zadd(
            queue,
            store.toDouble(),
            payment
        )
    }

     fun dequeue(reverse: Boolean): String? {
        if (!isSetup)
            setup()

        val tuple = if (reverse) {
            jedis.zpopmax(queue)
        } else {
            jedis.zpopmin(queue)
        }

         return tuple?.element
    }
}
