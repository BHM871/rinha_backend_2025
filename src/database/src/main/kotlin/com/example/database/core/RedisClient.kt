package com.example.database.core

import redis.clients.jedis.ConnectionPoolConfig

interface RedisClient {
    val host: String
    val port: Int
    val poolConfig: ConnectionPoolConfig
    fun setup()
}