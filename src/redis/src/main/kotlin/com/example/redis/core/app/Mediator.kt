package com.example.redis.core.app

interface Mediator {
    fun notify(component: Component, data: Any? = null) : Any?
}