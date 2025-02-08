package com.umar.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ChatEvent {
    abstract val type: String
}

@Serializable
@SerialName("message")
data class MessageEvent(
    override val type: String,
    val data: List<MessageData>
) : ChatEvent()

@Serializable
data class MessageData(
    val text: String
)

@Serializable
@SerialName("status")
data class StatusEvent(
    override val type: String,
    val data: List<StatusData>
) : ChatEvent()

@Serializable
data class StatusData(
    val status: String,
    val remotejid: String
)