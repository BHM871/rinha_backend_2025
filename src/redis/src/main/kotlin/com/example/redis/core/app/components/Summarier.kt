package com.example.redis.core.app.components

import com.example.models.core.FilterSummary
import com.example.models.core.Summary
import com.example.redis.core.app.Component

interface Summarier : Component {
    fun getSummary(filter: FilterSummary) : Summary
}