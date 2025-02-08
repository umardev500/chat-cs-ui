package com.umar.chat.repository

import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.network.ChatApiService
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val apiService: ChatApiService) {

    // Fetch user data from the API
    suspend fun getChats(): ChatResponse {
        return apiService.fetchChats()
    }

    fun listenToChatEvents(): Flow<String> = apiService.listenToChatEvents()
}