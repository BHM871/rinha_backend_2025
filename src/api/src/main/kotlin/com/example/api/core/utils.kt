package com.example.api.core

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Formatters {
    companion object {
        val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}

fun now() : LocalDateTime {
    return LocalDateTime.now(ZoneOffset.UTC)
}