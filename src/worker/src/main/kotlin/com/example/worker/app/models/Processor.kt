package com.example.worker.app.models

import com.example.worker.app.interfaces.Gateway

data class DefaultProcessor(
    val client: Gateway
)

data class FallbackProcessor(
    val client: Gateway
)
