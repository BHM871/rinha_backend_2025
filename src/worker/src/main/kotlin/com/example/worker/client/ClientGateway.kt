package com.example.worker.client

import com.example.worker.app.interfaces.Gateway
import com.example.worker.app.models.Health
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

class ClientGateway(
    override val host: String,
    override val port: Int
) : Gateway {

    private val client by lazy { HttpClient {
        install(ContentNegotiation) {
            json()
        }
    } }

    override suspend fun processor(payment: String, timeout: Long): Boolean? {
        try {
            val response = client.post("http://$host:$port/payments") {
                timeout {
                    requestTimeoutMillis = timeout
                }
                contentType(ContentType.Application.Json)
                setBody(payment)
            }

            if (response.status.value == 422) return null

            return response.status.value == 200
        } catch (_: Exception) {
            return false
        }
    }

    override suspend fun health(): Health {
        val response = client.get("http://$host:$port/payments/service-health") {
            contentType(ContentType.Application.Json)
        }

        return response.body<Health>()
    }
}
