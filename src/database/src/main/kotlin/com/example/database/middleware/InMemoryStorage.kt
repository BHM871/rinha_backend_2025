package com.example.database.middleware

import com.example.models.core.FilterSummary
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

class InMemoryStorage {
    companion object {
        private val default = hashMapOf<Long, Pair<Int, BigDecimal>>()
        private val fallback = hashMapOf<Long, Pair<Int, BigDecimal>>()
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        val millis = date.toMillis()
        if (isDefault) {
            val current = default[millis]
            default[millis] = Pair((current?.first ?: 0) + 1, score.add(current?.second ?: BigDecimal.ZERO))
        } else {
            val current = fallback[millis]
            fallback[millis] = Pair((current?.first ?: 0) + 1, score.add(current?.second ?: BigDecimal.ZERO))
        }
    }

    fun getSummary(filter: FilterSummary): Summary {
        val fromMillis = filter.from?.toMillis() ?: 0
        val toMillis = filter.to?.toMillis() ?: Long.MAX_VALUE

        var requests = 0
        var amount = BigDecimal.ZERO
        default.entries
                .filter { it.key >= fromMillis && it.key <= toMillis }
                .forEach { amount = amount.add(it.value.second); requests += it.value.first }
        val defaultProcessor = ProcessorInfos(
            requests,
            amount
        )

        requests = 0
        amount = BigDecimal.ZERO
        fallback.entries
            .filter { it.key >= fromMillis && it.key <= toMillis }
            .forEach { amount = amount.add(it.value.second); requests += it.value.first }
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