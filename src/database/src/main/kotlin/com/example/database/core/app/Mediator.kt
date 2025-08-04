package com.example.database.core.app

interface Mediator {
    fun notify(component: Component, event: Event, vararg data: Any?) : Any?
}