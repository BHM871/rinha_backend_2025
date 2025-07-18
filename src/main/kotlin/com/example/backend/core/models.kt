package com.example.backend.core

import com.example.backend.plugins.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Payment(
    val correlationId: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal
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