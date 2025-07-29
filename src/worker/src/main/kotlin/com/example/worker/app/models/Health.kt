package com.example.worker.app.models

import kotlinx.serialization.Serializable

@Serializable
data class Health(
    val failing: Boolean,
    val minResponseTime: Long
)