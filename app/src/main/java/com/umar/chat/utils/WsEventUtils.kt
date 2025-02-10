package com.umar.chat.utils

import com.umar.chat.data.model.Message
import com.umar.chat.data.model.MessageType
import com.umar.chat.data.model.RawWsResponse
import com.umar.chat.data.model.Status
import com.umar.chat.data.model.WsEvent
import com.umar.chat.data.model.wsModule
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class WsEventUtils private constructor() {
    companion object {
        private val json = Json {
            serializersModule = wsModule
            ignoreUnknownKeys = true
        }

        fun getJsonSerializer() = json

        fun parseRawWsResponse(rawWsResponse: String): RawWsResponse {
            return json.decodeFromString(RawWsResponse.serializer(), rawWsResponse)
        }

        fun parseWsEvents(rawWsResponse: RawWsResponse): List<WsEvent> {
            return when (rawWsResponse.mt) {
                MessageType.Status.mt -> rawWsResponse.data.map {
                    json.decodeFromJsonElement(
                        Status.serializer(),
                        it
                    )
                }

                MessageType.Message.mt -> rawWsResponse.data.map {
                    json.decodeFromJsonElement(
                        Message.serializer(),
                        it
                    )
                }

                else -> throw SerializationException("Unknown message type: ${rawWsResponse.mt}")
            }
        }
    }
}