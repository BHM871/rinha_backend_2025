package com.example.redis.core.app

interface Mediator {
    fun notify(component: Component, event: Event, data: Any? = null) : Any?
}