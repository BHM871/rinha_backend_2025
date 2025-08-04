package com.example.redis.middleware

import com.example.models.core.Payment
import java.math.BigDecimal
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.collections.set

class InMemoryQueue {

    companion object {
        private val queue: MutableMap<Long, BlockingQueue<Payment>> = hashMapOf()
    }

    fun enqueue(payment: Payment) {
        val score = payment.score.toLong()
        if (!queue.containsKey(score))
            queue[score] = createQueue()

        queue[score]!!.add(payment)
    }

    fun dequeue(onTop: Boolean) : Payment? {
        return if (!onTop) queue.values.firstOrNull { it.isNotEmpty() }?.poll()
        else queue.values.lastOrNull() { it.isNotEmpty() }?.poll()
    }

    private fun createQueue() = PriorityBlockingQueue<Payment>(
        100,
        Comparator { o1, o2 ->
            o1.score
                .minus(o2.score)
                .multiply(BigDecimal.valueOf(10000))
                .toInt()
        }
    )
}
