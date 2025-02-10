package com.umar.chat.ui.components.organisms

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.umar.chat.data.model.ChatData
import com.umar.chat.data.model.Status
import com.umar.chat.ui.components.molecules.ChatItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    chats: List<ChatData>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNavigate: (jid: String) -> Unit,
    statusUpdate: List<Status>
) {
    Log.d("ChatLog", "Status update: $statusUpdate")

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            if (chats.isEmpty()) {
                item {
                    Box (
                        modifier = Modifier
                            .fillParentMaxSize()
                    )
                }
            }

            items(chats, key = { chat -> chat.remotejid + chat.csid }) { chat ->
                ChatItem(chat, navigate = onNavigate)
            }
        }
    }
}