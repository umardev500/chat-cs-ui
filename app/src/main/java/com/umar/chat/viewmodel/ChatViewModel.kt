package com.umar.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umar.chat.data.model.ChatResponse
import com.umar.chat.data.model.Message
import com.umar.chat.data.model.MessageType
import com.umar.chat.data.model.Status
import com.umar.chat.data.model.Typing
import com.umar.chat.repository.ChatRepository
import com.umar.chat.repository.WebsocketRepository
import com.umar.chat.utils.WsEventResult
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

    private val _typingUpdate = MutableStateFlow<List<Typing>>(emptyList())
    val typingUpdate: StateFlow<List<Typing>> = _typingUpdate

    private val _chatUpdate = MutableStateFlow<Message?>(null)
    val chatUpdate: StateFlow<Message?> = _chatUpdate

    init {
        fetchChat()
        listentToWebsocketEvents()
    }

    // Reset all state and refetch on refresh
    fun handleRefresh() {
        fetchChat()
        listentToWebsocketEvents()
    }

    private fun listentToWebsocketEvents() {
        viewModelScope.launch {
            websocketRepository.listenWebsocketEvents()
                .catch { e -> Log.e("ChatScreen", "Flow error: ${e.message}", e) }
                .collect { ev ->
                    val rawResponse = WsEventUtils.parseRawWsResponse(ev) ?: return@collect

                    val data: WsEventResult =
                        WsEventUtils.parseWsEvents(rawResponse) ?: return@collect

                    when (data) {
                        is WsEventResult.Multiple -> {
                            val events = data.events
                            when (rawResponse.mt) {
                                MessageType.Status.mt -> {
                                    val statuses = events.filterIsInstance<Status>()
                                    _statusUpdate.update { statuses }
                                }

                                MessageType.Typing.mt -> {
                                    val typings = events.filterIsInstance<Typing>()
                                    _typingUpdate.update { typings }
                                }
                            }
                        }

                        is WsEventResult.Single -> {
                            when (rawResponse.mt) {
                                MessageType.Message.mt -> {
                                    val message = data.event as? Message
                                    if (message != null) {
                                        _typingUpdate.update { emptyList() } // reset typing state after sent

                                        if (message.isInitial) {
                                            // ✅ Initialize chats
                                            appendOrInitializeChats(message)

                                            // ✅ Update unread count
                                            message.textMessage?.metadata?.let {
                                                updateUnreadCount(
                                                    it.remotejid
                                                )
                                            }
                                        } else {
                                            // ✅ Update higlight message
                                            _chatUpdate.update { message }

                                            // ✅ Update unread count
                                            message.textMessage?.metadata?.let {
                                                updateUnreadCount(
                                                    it.remotejid
                                                )
                                            } // update unread count
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun appendOrInitializeChats(message: Message) {
        _chatUiState.update { currentState ->
            val currentChatResponse = currentState.chatResponse?.let {
                it.copy(
                    data = (message.initialChats ?: emptyList()) + it.data
                )
            } ?: message.initialChats?.let {
                ChatResponse(
                    success = true,
                    message = "Initialized",
                    data = it
                )
            }
            currentState.copy(chatResponse = currentChatResponse)
        }
    }

    private fun updateUnreadCount(remoteJid: String) {
        viewModelScope.launch {
            val currentChatResponse = _chatUiState.value.chatResponse ?: return@launch

            // Cs id temporary variable
            var csId: String? = null

            // ✅ Update unread count in chatResponse
            val updatedChats = currentChatResponse.data.map { chat ->
                if (chat.lastMessage?.metadata?.remotejid == remoteJid) {
                    csId = chat.csid
                    chat.copy(unreadCount = chat.unreadCount + 1)
                } else {
                    chat
                }
            }

            // ✅ Persist changes to UI state
            _chatUiState.update {
                it.copy(chatResponse = currentChatResponse.copy(data = updatedChats))
            }

            // ✅ Update unread count in database (optional)
            csId?.let { chatRepository.updateUnreadCount(remoteJid, it) }
        }
    }

    // fetchChat is function to fetch chat data
    private fun fetchChat() {
        viewModelScope.launch {
            _chatUiState.update { it.copy(isLoading = true) }
            delay(100)

            try {
                val response = chatRepository.getChats()
                _chatUiState.update { it.copy(chatResponse = response, isLoading = false) }
            } catch (e: Exception) {
                Log.d("ChatScreen", "Failed to fetch chats: ${e.localizedMessage}")
                _chatUiState.update { it.copy(isLoading = false) } // ✅ Ensure loading state is reset
            } finally {
                _chatUpdate.update { null }
            }
        }
    }
}