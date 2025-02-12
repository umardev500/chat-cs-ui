package com.umar.chat.ui.components.molecules

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umar.chat.data.model.ChatData
import com.umar.chat.data.model.CommonModel
import com.umar.chat.ui.components.atoms.Avatar
import com.umar.chat.utils.formatEpochTime
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun ChatItem(
    chat: ChatData,
    navigate: (jid: String) -> Unit,
    getProfilePic: suspend (jid: String) -> String?
) {
    var profilePicUrl by remember { mutableStateOf<String?>(null) }

    val isUnread = chat.unreadCount > 0

    LaunchedEffect(chat.remotejid) {
        val pic = getProfilePic(chat.remotejid)
        if (pic != null) {
            profilePicUrl = pic
        }
    }

    Row(
        modifier = Modifier
            .clickable {
                navigate(chat.remotejid)
            }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Avatar(imageUrl = profilePicUrl, isOnline = chat.isOnline)
        Column(
            modifier = Modifier
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Chat Name and Time
            ChatNameWithTime(
                chatName = chat.lastMessage?.pushname ?: chat.remotejid,
                timestamp = formatEpochTime(chat.lastMessage?.timestamp ?: 0L),
                isUnread
            )

            // Bottom
            ChatMessageDetails(
                lastMessage = chat.lastMessage!!,
                count = chat.unreadCount,
                isTyping = chat.isTyping
            )
        }

    }
}