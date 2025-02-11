package com.umar.chat.data.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

sealed class MessageType(val mt: String) {
    data object Status : MessageType("status")
    data object Typing : MessageType("typing")
    data object Message : MessageType("message")
}

@Serializable
@Polymorphic
sealed class WsEvent

@Serializable
@SerialName("status")
data class Status(
    val status: String,
    val remotejid: String
) : WsEvent()

@Serializable
@SerialName("message")
data class Message(
    val isInitial: Boolean,
    @SerialName("initial_chats") val initialChats: List<ChatData>? = null,
    val textMessage: TextMessage? = null,
) : WsEvent()

@Serializable
data class TextMessage(
    val conversation: String,
    val pushName: String,
    val timestamp: Long,
    val metadata: MetaData
)

@Serializable
data class MetaData(
    @SerialName("remoteJid") val remotejid: String,
    val fromMe: Boolean = false,
    val id: String
)

@Serializable
@SerialName("typing")
data class Typing(
    val presence: String,
    val remotejid: String
) : WsEvent()

@Serializable
data class RawWsResponse(
    val mt: String,
    val data: JsonElement
)

@Serializable
data class WsResponse(
    val mt: String,
    val data: List<WsEvent>
)

val wsModule = SerializersModule {
    polymorphic(WsEvent::class) {
        subclass(Status::class, Status.serializer())
        subclass(Message::class, Message.serializer())
        subclass(Typing::class, Typing.serializer())
    }
}
