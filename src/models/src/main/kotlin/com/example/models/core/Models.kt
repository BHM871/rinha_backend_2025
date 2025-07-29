package com.example.models.core

import com.example.models.plugins.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

class Payment {
    companion object {
        fun getScore(payment: String) : BigDecimal {
            return payment.substring(
                payment.indexOfLast { it == ':' } + 1,
                payment.indexOfLast { it == '}' }
            ).trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
        }

        fun addNow(payment: String) : String {
            val pay = payment.substring(1, payment.length)
            val now = "\"requestedAt\":\"${now().format(Formatters.localDateTimeFormatter)}\""
            return "{$now,$pay"
        }

        fun getDate(payment: String) : LocalDateTime {
            val date = payment.substring(
                payment.indexOfFirst { it == ':' } + 1,
                payment.indexOfFirst { it == ',' }
            )

            return LocalDateTime.parse(date.trim().substring(1, date.lastIndex), Formatters.localDateTimeFormatter)
        }
    }
}

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