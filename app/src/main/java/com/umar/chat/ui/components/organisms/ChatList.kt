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
import com.umar.chat.data.model.ChatEvent
import com.umar.chat.data.model.StatusEvent
import com.umar.chat.ui.components.molecules.ChatItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    chats: List<ChatData>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNavigate: (jid: String) -> Unit,
    chatEventMap: Map<String, ChatEvent>
) {

    Log.d("ChatScreen", chatEventMap.toString())

    val updateChats = chats.map { chat ->
        val matching = chatEventMap.values.find { ev ->
            when (ev) {
                is StatusEvent -> ev.data.remotejid == chat.remotejid
                else -> false
            }
        }

        when (matching) {
            is StatusEvent -> chat.copy(
                isOnline = matching.data.status == "online"
            )
            else -> chat
        }
    }


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(updateChats, key = { chat -> chat.remotejid + chat.csid }) { chat ->
                ChatItem(chat, navigate = onNavigate)
            }
        }
    }
}