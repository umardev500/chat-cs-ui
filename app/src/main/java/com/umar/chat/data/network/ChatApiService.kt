package com.umar.chat.data.network

import com.umar.chat.data.model.ChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class ChatApiService {
    private val host = "192.168.205.44"
    private val port = 8001
    private val baseUrl = "http://$host:$port/api"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }
        install(SSE)
    }

    fun listenToChatEvents(): Flow<String> = flow {
        try {
            client.sse(
                scheme = "http",
                host = host,
                port = port,
                path = "/api/chat/sse/980"
            ) {
                incoming.collect { ev ->
                    ev.data?.let { emit(it) }
                }
            }
        } catch (e: Exception) {
            throw e

        }
    }

    suspend fun fetchChats(): ChatResponse {
        val url = "$baseUrl/chat"
        return try {
            val response: HttpResponse = client.get(url) // Send get data
            response.body<ChatResponse>() // Deserialize response body to ChatResponse
        } catch (e: Exception) {
            // Hanlde any exceptions
            throw RuntimeException("Failed to fetch chats: ${e.localizedMessage}")
        }
    }
}