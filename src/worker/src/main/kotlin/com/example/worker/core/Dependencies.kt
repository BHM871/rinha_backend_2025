package com.example.worker.core

import kotlin.reflect.KClass

object Dependencies {
    val registry: MutableMap<KClass<*>, Any> = mutableMapOf()

    inline fun <reified T : Any> provide(dependency: T) {
        registry[T::class] = dependency
    }

    inline fun <reified T> resolve() : Any? {
        return registry[T::class]
    }
}

val Application.dependencies: Dependencies
    get() = Dependencies
