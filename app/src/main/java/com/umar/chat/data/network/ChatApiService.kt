package com.umar.chat.data.network

import android.util.Log
import com.umar.chat.BuildConfig
import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.model.CommonModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChatApiService {
    private val host = BuildConfig.API_HOST
    private val port = BuildConfig.API_PORT
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
    }

    suspend fun fetchProfilePic(jid: String): CommonModel {
        val url = "$baseUrl/chat/picture/$jid"
        return try {
            val response: HttpResponse = client.get(url) // Send get data
            response.body<CommonModel>()
        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch pic: ${e.localizedMessage}")
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

    suspend fun updateUnreadCount(jid: String, csid: String): CommonModel {
        val url = "$baseUrl/chat/update-unread-count?jid=$jid&csid=$csid"
        return try {
            val response: HttpResponse = client.get(url) // Send GET request
            response.body<CommonModel>()

        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch chats: ${e.localizedMessage}")
        }
    }
}