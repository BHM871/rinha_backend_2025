package com.example.worker.app.models

data class Health(
    val failing: Boolean,
    val minResponseTime: Long
)