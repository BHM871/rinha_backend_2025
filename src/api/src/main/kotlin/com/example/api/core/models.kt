package com.example.api.core

import com.example.api.plugins.BigDecimalSerializer
import com.example.api.plugins.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Payment(
    val correlationId: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val requestedAt: LocalDateTime? = null
)

@Serializable
data class Summary(
    val default: ProcessorInfos,
    val fallback: ProcessorInfos
)

@Serializable
data class ProcessorInfos(
    val totalRequests: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val totalAmount: BigDecimal
)

data class FilterSummary(
    val from: LocalDateTime? = null,
    val to: LocalDateTime? = null
) {
    constructor(from: String?, to: String?) : this(
        LocalDateTime.parse(from ?: "0001-01-01T00:00:00.000Z", Formatters.localDateTimeFormatter),
        LocalDateTime.parse(to ?: "9999-01-01T00:00:00.000Z", Formatters.localDateTimeFormatter)
    )
}

class Formatters {
    companion object {
        val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}