package com.example.redis.middleware

import com.example.models.core.FilterSummary
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

class InMemoryStorage {
    companion object {
        private val default = hashMapOf<Long, BigDecimal>()
        private val fallback = hashMapOf<Long, BigDecimal>()
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        if (isDefault) {
            val current = default[date.toMillis()]
            default[date.toMillis()] = score.add(current ?: BigDecimal.ZERO)
        } else {
            val current = fallback[date.toMillis()]
            fallback[date.toMillis()] = score.add(current ?: BigDecimal.ZERO)
        }
    }

    fun getSummary(filter: FilterSummary): Summary {
        val fromMillis = filter.from?.toMillis() ?: 0
        val toMillis = filter.to?.toMillis() ?: Long.MAX_VALUE

        var requests = 0
        var amount = BigDecimal.ZERO
        default.entries
                .parallelStream()
                .filter { it.key >= fromMillis && it.key <= toMillis }
                .forEach { amount.add(it.value); requests++ }
        val defaultProcessor = ProcessorInfos(
            requests,
            amount
        )

        requests = 0
        amount = BigDecimal.ZERO
        fallback.entries
            .parallelStream()
            .filter { it.key >= fromMillis && it.key <= toMillis }
            .forEach { amount.add(it.value); requests++ }
        val fallbackProcessor = ProcessorInfos(
            requests,
            amount
        )

        return Summary(
            defaultProcessor,
            fallbackProcessor
        )
    }

    private fun LocalDateTime.toMillis() : Long {
        return atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}