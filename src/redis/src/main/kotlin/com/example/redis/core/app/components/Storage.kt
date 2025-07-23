package com.example.redis.core.app.components

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.models.core.Summary
import com.example.redis.core.app.Component
import com.example.redis.core.app.Event

interface Storage : Component {
    fun store(payment: Payment) {
        TODO("Not Implemented Yet")
    }

    fun getSummary(filter: FilterSummary) : Summary {
        TODO("Not Implemented Yet")
    }
}