package com.umar.chat.ui.components.organisms

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umar.chat.data.model.ChatData
import com.umar.chat.data.model.ChatEventData
import com.umar.chat.ui.components.molecules.ChatItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    chats: List<ChatData>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNavigate: (jid: String) -> Unit,
    chatEventMap: Map<String, List<ChatEventData>>
) {
    val updatedChats = chats.map { chatData ->
        val matching = chatEventMap.values.flatten().find { chatEventData ->
            when (chatEventData) {
                is ChatEventData.Status -> chatEventData.remotejid == chatData.remotejid
                else -> false
            }
        }

        when (matching) {
            is ChatEventData.Status -> chatData.copy(
                isOnline = matching.status == "online"
            )

            else -> chatData
        }

    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(updatedChats, key = { chat -> chat.remotejid + chat.csid }) { chat ->
                ChatItem(chat, navigate = onNavigate)
            }
        }
    }
}