package com.umar.chat.ui.components.molecules

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umar.chat.R
import com.umar.chat.data.model.ChatData
import com.umar.chat.ui.components.atoms.Avatar
import com.umar.chat.utils.formatEpochTime

@Composable
fun ChatItem(chat: ChatData, navigate: (jid: String) -> Unit) {
    val isUnread = chat.unreadCount > 0

    Row(
        modifier = Modifier
            .clickable {
                navigate(chat.remotejid)
            }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Avatar(painter = painterResource(R.drawable.avatar), isOnline = chat.isOnline)
        Column(
            modifier = Modifier
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Chat Name and Time
            ChatNameWithTime(
                chatName = chat.lastMessage?.pushname ?: chat.remotejid,
                timestamp = formatEpochTime(chat.lastMessage?.timestamp ?: 0L),
                isUnread
            )

            // Bottom
            ChatMessageDetails(lastMessage = chat.lastMessage!!, count = chat.unreadCount)
        }

    }
}