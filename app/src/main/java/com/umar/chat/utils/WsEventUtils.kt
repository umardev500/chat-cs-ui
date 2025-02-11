package com.umar.chat.utils

import android.util.Log
import com.umar.chat.data.model.Message
import com.umar.chat.data.model.MessageType
import com.umar.chat.data.model.RawWsResponse
import com.umar.chat.data.model.Status
import com.umar.chat.data.model.Typing
import com.umar.chat.data.model.WsEvent
import com.umar.chat.data.model.wsModule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

sealed class WsEventResult {
    data class Single(val event: WsEvent) : WsEventResult()
    data class Multiple(val events: List<WsEvent>) : WsEventResult()
}


class WsEventUtils private constructor() {
    companion object {
        private val json = Json {
            serializersModule = wsModule
            ignoreUnknownKeys = true
        }

        fun getJsonSerializer() = json

        private inline fun <reified T : WsEvent> parseData(
            data: JsonElement,
            serializer: KSerializer<T>
        ): WsEventResult {
            return when (data) {
                is JsonArray -> WsEventResult.Multiple(data.map {
                    json.decodeFromJsonElement(
                        serializer,
                        it
                    )
                })

                else -> WsEventResult.Single(json.decodeFromJsonElement(serializer, data))
            }
        }

        fun parseRawWsResponse(rawWsResponse: String): RawWsResponse? {
            return try {
                json.decodeFromString(RawWsResponse.serializer(), rawWsResponse)
            } catch (e: Exception) {
                Log.d("ChatLog", "Failed to parse raw ws response: ${e.localizedMessage}")
                null
            }
        }

        fun parseWsEvents(rawWsResponse: RawWsResponse): WsEventResult? {
            return try {
                when (rawWsResponse.mt) {
                    MessageType.Status.mt -> parseData(rawWsResponse.data, Status.serializer())
                    MessageType.Message.mt -> parseData(rawWsResponse.data, Message.serializer())
                    MessageType.Typing.mt -> parseData(rawWsResponse.data, Typing.serializer())
                    else -> throw SerializationException("Unknown message type: ${rawWsResponse.mt}")
                }
            } catch (e: Exception) {
                Log.e("ChatLog", "Error parsing WsEvent: ${e.message}\nRaw Response: $rawWsResponse", e)
                null // Return null instead of crashing
            }
        }
    }
}