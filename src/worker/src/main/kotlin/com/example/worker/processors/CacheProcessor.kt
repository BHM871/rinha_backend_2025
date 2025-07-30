package com.example.worker.processors

import com.example.models.core.Payment
import com.example.worker.core.pool.Processor
import com.example.worker.repository.RedisRepository
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.locks.synchronized
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.util.PriorityQueue

class CacheProcessor(
    private val repository: RedisRepository,
    private val batch: Int
) : Processor {

    companion object {
        private val cache: MutableMap<Long, PriorityQueue<String>> = hashMapOf()

        fun enqueue(payment: String) {
            val score = Payment.getScore(payment).toLong()
            if (!cache.containsKey(score))
                cache[score] = createQueue()

            cache[score]!!.add(payment)
        }

        @OptIn(InternalAPI::class)
        fun dequeue(onTop: Boolean) : String? {
            synchronized(cache) {
                val a = if (!onTop) cache.values.first { it.isNotEmpty() }.poll()
                else cache.values.last { it.isNotEmpty() } .poll()
                println(cache)
                if (a!=null) println(a)
                return a
            }
        }

        private fun createQueue() = PriorityQueue<String>(
            Comparator { o1, o2 ->
                Payment.getScore(o1)
                    .minus(Payment.getScore(o2))
                    .multiply(BigDecimal.valueOf(10000))
                    .toInt()
            }
        )
    }

    override suspend fun process() {
        for (i in 1..batch){
            val payment = repository.dequeue()
            if (payment == null)
                break

            enqueue(payment)
        }
        delay(50)
    }
}