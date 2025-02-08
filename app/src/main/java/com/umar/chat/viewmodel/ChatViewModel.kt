package com.umar.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.chat.data.model.ChatEvent
import com.umar.chat.data.model.ChatEventData
import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.model.MessageEvent
import com.umar.chat.data.model.StatusEvent
import com.umar.chat.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class ChatUiState(
    val chatResponse: ChatResponse? = null,
    val isLoading: Boolean = false
)

object ChatEventSerializer : JsonContentPolymorphicSerializer<ChatEvent>(ChatEvent::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChatEvent> {
        return when (val type = element.jsonObject["type"]?.jsonPrimitive?.content) {
            "message" -> MessageEvent.serializer()
            "status" -> StatusEvent.serializer()
            else -> throw SerializationException("Unknown type: $type")
        }
    }
}

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private val _chatEventMap = MutableStateFlow<Map<String, List<ChatEventData>>>(emptyMap())
    val chatEventMap: StateFlow<Map<String, List<ChatEventData>>> = _chatEventMap

    init {
        fetchChat()
        listenToChatEvents()
    }

    fun listenToChatEvents() {
        viewModelScope.launch {
            chatRepository.listenToChatEvents()
                .catch { e -> Log.e("ChatScreen", "Flow error: ${e.message}", e) }
                .collect { ev ->
                    val parsedEvent = try {
                        Json.decodeFromString(ChatEventSerializer, ev)
                    } catch (e: Exception) {
                        Log.d("ChatScreen", "${e.message}")
                        null
                    }

                    parsedEvent?.let { event ->
                        val eventType = event.type
                        _chatEventMap.update { curState ->
                            val updatedState = curState.toMutableMap()

                            if (event is StatusEvent) {
                                val statusList =
                                    updatedState[eventType]?.filterIsInstance<ChatEventData.Status>()
                                        ?: emptyList()
                                val updatedStatusList = statusList.toMutableList()

                                for (newStatus in event.data) {
                                    val index =
                                        updatedStatusList.indexOfFirst { it.remotejid == newStatus.remotejid }
                                    if (index != -1) {
                                        // Update existing status
                                        updatedStatusList[index] =
                                            updatedStatusList[index].copy(status = newStatus.status)
                                    } else {
                                        // Append new status
                                        updatedStatusList.add(
                                            ChatEventData.Status(
                                                status = newStatus.status,
                                                remotejid = newStatus.remotejid
                                            )
                                        )
                                    }
                                }

                                updatedState[eventType] = updatedStatusList
                            }

                            updatedState
                        }
                    }

                }
        }
    }

    fun fetchChat() {
        viewModelScope.launch {
            _chatUiState.update { it.copy(isLoading = true) }

            try {
                val response = chatRepository.getChats()
                _chatUiState.update { it.copy(chatResponse = response, isLoading = false) }
            } catch (e: Exception) {
                Log.d("ChatScreen", "Failed to fetch chats: ${e.localizedMessage}")
                _chatUiState.update { it.copy(isLoading = false) } // ✅ Ensure loading state is reset
            }
        }
    }
}