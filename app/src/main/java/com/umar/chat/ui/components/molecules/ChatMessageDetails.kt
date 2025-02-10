package com.umar.chat.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.umar.chat.data.model.LastMessage
import com.umar.chat.ui.components.atoms.MessageCount
import com.umar.chat.ui.theme.Gray600
import com.umar.chat.ui.theme.Green600

@Composable
fun ChatMessageDetails(lastMessage: LastMessage, count: Int, isTyping: Boolean = false) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Higlight message
        if (isTyping) {
            Text(
                text = "Typing...",
                color = Green600,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = lastMessage.conversation,
                color = Gray600,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        if (count > 0) {
            MessageCount(count = count)
        }
    }
}