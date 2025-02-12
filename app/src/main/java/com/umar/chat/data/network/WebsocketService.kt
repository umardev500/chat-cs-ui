package com.umar.chat.data.network

import android.util.Log
import com.umar.chat.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WebsocketService {
    private val _host = BuildConfig.API_HOST
    private val _port = BuildConfig.API_PORT

    private val _client = HttpClient(CIO) {
        install(WebSockets)
    }

    private val _messageFlow = MutableSharedFlow<String>()

    fun listenWebsocketEvents(): Flow<String> = flow {
        _client.webSocket(
            method = HttpMethod.Get,
            host = _host,
            port = _port,
            path = "/api/ws?token=xyz"
        ) {
            val sendJob = launch {
                _messageFlow.collect { message ->
                    send(Frame.Text(message))
                }
            }

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        emit(frame.readText())
                    }

                    else -> Unit
                }
            }

            sendJob.cancel()

            val reason = closeReason.await()
            Log.d("ChatScreen", "WebSocket closed with reason: $reason")
        }
    }

    suspend fun sendMessage(message: String) {
        _messageFlow.emit(message)
    }
}