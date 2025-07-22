package com.example.redis.core

interface RedisClient {
    val host: String
    val port: Int
    fun setup()
}