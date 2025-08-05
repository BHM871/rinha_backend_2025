package com.example.models.core

import com.example.models.plugins.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

class Payment(
    payment: String
) {

    var buffer = StringBuilder(payment)

    val body: String
        get() = buffer.toString()

    private var _score: BigDecimal? = null
    val score: BigDecimal
        get() = _score ?: score()

    private var _date: LocalDateTime? = null
    val date: LocalDateTime
        get() {
            addNow()
            return _date!!
        }

    private fun score() : BigDecimal {
        _score = buffer.substring(
            buffer.lastIndexOf(':') + 1,
            buffer.lastIndexOf('}')
        ).trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
        return _score!!
    }

    fun addNow() {
        if (_date != null || date() != null) return

        buffer.deleteAt(buffer.indexOf('{'))
        buffer.insert(0,"{\"requestedAt\":\"", 0, 16)
        buffer.insert(16, now().format(Formatters.localDateTimeFormatter), 0, 24)
        buffer.insert(40, "\",", 0, 2)

        date()
    }

    private fun date() : LocalDateTime? {
        val date = buffer.substring(
            buffer.indexOf(':') + 2,
            buffer.indexOf(',') - 1
        )

        try {
            _date = LocalDateTime.parse(date.trim(), Formatters.localDateTimeFormatter)
            return _date!!
        } catch (_: Exception) {
            return null
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