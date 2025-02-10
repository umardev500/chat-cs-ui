package com.umar.chat.repository

import com.umar.chat.data.network.WebsocketService
import kotlinx.coroutines.flow.Flow

class WebsocketRepository(private val service: WebsocketService) {
    fun listenWebsocketEvents(): Flow<String> = service.listenWebsocketEvents()
}