package com.umar.chat.data.model

import com.umar.chat.utils.WsEventUtils
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
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
    val message: String,
    val remotejid: String
) : WsEvent()

@Serializable
@SerialName("typing")
data class Typing(
    val presence: String,
    val remotejid: String
) : WsEvent()

@Serializable
data class RawWsResponse(
    val mt: String,
    val data: JsonArray
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

fun main() {
    val jsonString = """{
        "mt": "message",
        "data": [
            {
                "remotejid": "6285123456791@s.whatsapp.net",
                "message": "online"
            }
        ]
    }"""


    val json = Json {
        serializersModule = wsModule
        ignoreUnknownKeys = true
    }

    val rawResponse = json.decodeFromString<RawWsResponse>(jsonString)
    val data: List<WsEvent> = WsEventUtils.parseWsEvents(rawResponse)

    val wsResponse = WsResponse(rawResponse.mt, data)
    println(wsResponse)

    if (wsResponse.mt == "status") {
        val statuses = wsResponse.data.filterIsInstance<Status>()
    }
}