package com.example.worker.core

import kotlin.reflect.KClass

object Dependencies {
    val registry: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> provide(type: KClass<T>, dependency: T) {
        registry[type] = dependency
    }

    fun <T : Any> resolve(type: KClass<T>) : Any? {
        return registry[type]
    }
}

val Application.dependencies: Dependencies
    get() = Dependencies
