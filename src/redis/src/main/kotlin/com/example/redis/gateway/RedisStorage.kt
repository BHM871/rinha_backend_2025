package com.example.redis.gateway

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import com.example.redis.core.RedisClient
import redis.clients.jedis.ConnectionPoolConfig
import redis.clients.jedis.JedisPooled
import java.math.BigDecimal
import java.time.LocalDateTime

class RedisStorage(
    override val host: String,
    override val port: Int,
    override val poolConfig: ConnectionPoolConfig
) : RedisClient {

    companion object {
        private lateinit var jedis: JedisPooled
    }

    private var isSetup = false
    private val storage = "payments:storage"

    override fun setup() {
        if (!isSetup) {
            jedis = JedisPooled(poolConfig, host, port)
            isSetup = true
        }
    }

    fun store(store: BigDecimal, data: LocalDateTime) {
        if (!isSetup)
            setup()

        println("Storaged")
    }

    fun getSummary(filter: FilterSummary): Summary {
        if (!isSetup)
            setup()

        println("Getting Summary")
        return Summary(
            ProcessorInfos(0, BigDecimal.valueOf(0)),
            ProcessorInfos(0, BigDecimal.valueOf(0))
        )
    }
}