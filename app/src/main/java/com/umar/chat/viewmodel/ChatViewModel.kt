package com.umar.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.model.MessageType
import com.umar.chat.data.model.Status
import com.umar.chat.data.model.WsEvent
import com.umar.chat.repository.ChatRepository
import com.umar.chat.repository.WebsocketRepository
import com.umar.chat.utils.WsEventUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val chatResponse: ChatResponse? = null,
    val isLoading: Boolean = false,
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val websocketRepository: WebsocketRepository
) : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private val _statusUpdate = MutableStateFlow<List<Status>>(emptyList())
    val statusUpdate: StateFlow<List<Status>> = _statusUpdate

    init {
        fetchChat()
        listentToWebsocketEvents()
    }

    fun listentToWebsocketEvents() {
        viewModelScope.launch {
            websocketRepository.listenWebsocketEvents()
                .catch { e -> Log.e("ChatScreen", "Flow error: ${e.message}", e) }
                .collect { ev ->
                    val rawResponse = WsEventUtils.parseRawWsResponse(ev)
                    val data: List<WsEvent> = WsEventUtils.parseWsEvents(rawResponse)
                    when (rawResponse.mt) {
                        MessageType.Status.mt -> {
                            val statuses = data.filterIsInstance<Status>()
                            _statusUpdate.update { statuses }
                        }
                    }
                }
        }
    }

    // fetchChat is function to fetch chat data
    fun fetchChat() {
        viewModelScope.launch {
            _chatUiState.update { it.copy(isLoading = true) }
            delay(100)

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