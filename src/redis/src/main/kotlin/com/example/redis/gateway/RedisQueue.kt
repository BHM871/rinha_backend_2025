package com.example.redis.gateway

import com.example.models.core.Payment
import com.example.models.plugins.getSerializer
import com.example.redis.core.RedisClient
import redis.clients.jedis.ConnectionPoolConfig
import redis.clients.jedis.JedisPooled
import java.math.BigDecimal

class RedisQueue(
    override val host: String,
    override val port: Int
) : RedisClient {

    companion object {
        private lateinit var jedis: JedisPooled
    }

    private var isSetup = false
    private val queue = "payments:queue"

    override fun setup() {
        if (!isSetup) {
            println("Redis = $host:$port")
            val poolConfig = ConnectionPoolConfig().apply {
                maxTotal = 50
                jmxEnabled = false
            }
            jedis = JedisPooled(poolConfig, host, port)
            isSetup = true
        }
    }

    fun enqueue(payment: Payment) {
        if (!isSetup)
            setup()

        jedis.zadd(
            queue,
            payment.amount.divide(BigDecimal.valueOf(1000)).toDouble(),
            getSerializer().encodeToString(payment)
        )
    }

     fun dequeue(reverse: Boolean): Payment? {
        if (!isSetup)
            setup()

        val tuple = if (reverse) {
            jedis.zpopmax(queue)
        } else {
            jedis.zpopmin(queue)
        }

        return if (tuple == null) null
        else getSerializer().decodeFromString<Payment>(tuple.element)
    }
}
