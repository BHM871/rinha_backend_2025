package com.example.redis.gateway

import com.example.models.core.FilterSummary
import com.example.models.core.ProcessorInfos
import com.example.models.core.Summary
import com.example.redis.core.RedisClient
import redis.clients.jedis.ConnectionPoolConfig
import redis.clients.jedis.JedisPooled
import redis.clients.jedis.search.FTCreateParams
import redis.clients.jedis.search.IndexDataType
import redis.clients.jedis.search.aggr.AggregationBuilder
import redis.clients.jedis.search.aggr.Reducers
import redis.clients.jedis.search.aggr.Row
import redis.clients.jedis.search.schemafields.NumericField
import redis.clients.jedis.search.schemafields.SchemaField
import redis.clients.jedis.search.schemafields.TextField
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset


class RedisStorage(
    override val host: String,
    override val port: Int,
    override val poolConfig: ConnectionPoolConfig
) : RedisClient {

    companion object {
        private lateinit var jedis: JedisPooled
        private var isSetup = false
        private var stored = 0
    }

    private val key = "payments"
    private val idx = "idx:$key"

    override fun setup() {
        if (!isSetup) {
            jedis = JedisPooled(poolConfig, host, port)

            val schema = arrayOf<SchemaField>(
                TextField.of("processor"),
                NumericField.of("date"),
                NumericField.of("amount")
            )

            val result = try {
                jedis.ftInfo(idx)
                "OK"
            } catch (_: Exception) {
                jedis.ftCreate(
                    idx,
                    FTCreateParams.createParams()
                        .on(IndexDataType.HASH)
                        .addPrefix(key),
                    *schema
                )
            }

            isSetup = result == "OK"
        }
    }

    fun store(score: BigDecimal, date: LocalDateTime, isDefault: Boolean) {
        if (!isSetup)
            setup()

        jedis.hset(
            "$key:${stored++}",
            mapOf(
                Pair("processor", "$isDefault"),
                Pair("date", date.toMillis().toString()),
                Pair("amount", score.toString())
            )
        )
    }

    fun getSummary(filter: FilterSummary): Summary {
        if (!isSetup)
            setup()

        val result = jedis.ftAggregate(
            idx,
            AggregationBuilder("@date:[${filter.from?.toMillis() ?: "-inf"} ${filter.to?.toMillis() ?: "+inf"}]")
                .groupBy("@processor", Reducers.sum("amount").`as`("amount"), Reducers.count().`as`("size"))
        )

        var default = createInfosEmpty()
        var fallback = createInfosEmpty()

        for (row in result.rows) {
            if (row.get("processor") == "true")
                default = createInfos(row)
            else
                fallback = createInfos(row)
        }

        return Summary(default, fallback)
    }

    private fun LocalDateTime.toMillis() : Long {
        return atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    private fun createInfosEmpty() : ProcessorInfos {
        return ProcessorInfos(0, BigDecimal.ZERO)
    }

    private fun createInfos(row: Row) : ProcessorInfos {
        return ProcessorInfos(
            (row.get("size") as String).toInt(),
            (row.get("amount") as String).toBigDecimal()
        )
    }
}