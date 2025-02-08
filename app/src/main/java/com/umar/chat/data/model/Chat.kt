package com.umar.chat.data.model

import kotlinx.serialization.Serializable

// Main API response
@Serializable
data class ChatResponse(
    val success: Boolean,
    val message: String,
    val data: List<ChatData>,
)

// Chat data list
@Serializable
data class ChatData(
    val status: String,
    val unreadCount: Int,
    val lastMessage: LastMessage?,
    val remotejid: String,
    val csid: String,
    val isOnline: Boolean = false
)

// Last message details
@Serializable
data class LastMessage(
    val conversation: String,
    val pushname: String,
    val timestamp: Long,
    val metadata: MessageMetadata
)

// Message metadata
@Serializable
data class MessageMetadata(
    val remotejid: String,
    val fromme: Boolean,
    val id: String
)

@Serializable
sealed class ChatEventData {
    @Serializable
    data class Status(val status: String, val remotejid: String) : ChatEventData()

    @Serializable
    data class Message(val text: String) : ChatEventData()
}