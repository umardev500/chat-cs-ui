package com.umar.chat.repository

import android.util.Log
import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.model.CommonModel
import com.umar.chat.data.network.ChatApiService

class ChatRepository(private val apiService: ChatApiService) {

    suspend fun fetchProfilePic(jid: String): CommonModel {
        return apiService.fetchProfilePic(jid = jid)
    }

    // Fetch user data from the API
    suspend fun getChats(): ChatResponse {
        return apiService.fetchChats()
    }

    suspend fun updateUnreadCount(jid: String, csid: String) {
        try {
            val response = apiService.updateUnreadCount(jid = jid, csid = csid)
            Log.d("ChatLog", "📢 Update counter response: $response") // Log response
        } catch (e: Exception) {
            Log.e("ChatLog", "❌ Failed to update unread count: ${e.localizedMessage}", e)
        }
    }
}