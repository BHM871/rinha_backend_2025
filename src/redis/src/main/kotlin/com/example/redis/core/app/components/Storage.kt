package com.example.redis.core.app.components

import com.example.models.core.FilterSummary
import com.example.models.core.Summary
import com.example.redis.core.app.Component
import java.math.BigDecimal
import java.time.LocalDateTime

interface Storage : Component {
    fun store(store: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        TODO("Not Implemented Yet")
    }

    fun getSummary(filter: FilterSummary) : Summary {
        TODO("Not Implemented Yet")
    }
}