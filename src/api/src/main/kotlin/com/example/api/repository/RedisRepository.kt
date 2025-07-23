package com.example.api.repository

import com.example.models.core.FilterSummary
import com.example.models.core.Payment
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import com.example.redis.core.app.Event
import com.example.redis.core.app.Mediator
import com.example.redis.core.app.components.Queuer
import com.example.redis.core.app.components.Storage
import java.math.BigDecimal

class RedisRepository(
    override val mediator: Mediator
) : Queuer, Storage {
    override fun enqueue(payment: Payment) {
        this.mediator.notify(this, Event.ENQUEUE, payment)
    }

    override fun getSummary(filter: FilterSummary): Summary {
        return (
                this.mediator.notify(this, Event.SUMMARY, filter)
                    ?: Summary(
                        ProcessorInfos(0, BigDecimal.ZERO),
                        ProcessorInfos(0, BigDecimal.ZERO)
                    )
                ) as Summary
    }
}