package com.example.worker.core

import kotlin.reflect.KClass

object Dependencies {
    private val registry: MutableMap<KClass<*>, () -> Any> = mutableMapOf()

    fun <T : Any> provide(type: KClass<T>, dependency: () -> T) {
        registry[type] = dependency
    }

    fun <T : Any> resolve(type: KClass<T>) : T? {
        return registry[type]?.invoke() as T?
    }
}

val Worker.dependencies: Dependencies
    get() = Dependencies
