package com.umar.chat.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umar.chat.data.network.ChatApiService
import com.umar.chat.navigation.LocalNavigationActions
import com.umar.chat.navigation.NavigationActions
import com.umar.chat.repository.ChatRepository
import com.umar.chat.ui.components.organisms.ChatList
import com.umar.chat.viewmodel.ChatViewModel

@Composable
fun ChatScreen() {
    val chatApiService = remember { ChatApiService() }
    val chatRepository = remember { ChatRepository(apiService = chatApiService) }
    val chatViewModel: ChatViewModel = viewModel { ChatViewModel(chatRepository) }
    val chatUiState by chatViewModel.chatUiState.collectAsState()
    val chatEventMap by chatViewModel.chatEventsMap.collectAsState()

    val navigationActions: NavigationActions = LocalNavigationActions.current


    // Function to refresh chat data
    fun handleRefresh() {
        chatViewModel.fetchChat()
        chatViewModel.listenToChatEvents()
    }

    // Function to handle navigation to messaging screen
    fun handleNavigate(jid: String) {
        navigationActions.navigateToMessaging(jid)
    }

    Log.d("ChatScreen", "render")
    Log.d("ChatScreen", chatUiState.toString())

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ChatList(
                chats = chatUiState.chatResponse?.data ?: emptyList(),
                isRefreshing = false,
                onRefresh = ::handleRefresh,
                onNavigate = ::handleNavigate,
                chatEventMap
            )
        }
    }
}