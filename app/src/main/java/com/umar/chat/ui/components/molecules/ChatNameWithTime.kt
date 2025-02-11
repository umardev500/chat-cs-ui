package com.umar.chat.ui.components.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.chat.ui.theme.Gray600
import com.umar.chat.ui.theme.Gray700
import com.umar.chat.ui.theme.Green600

@Composable
fun ChatNameWithTime(
    chatName: String,
    timestamp: String,
    isUnread: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Chat Name
        Text(
            text = chatName,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gray700
            ),
            modifier = Modifier.weight(1f)
        )

        // Time
        Text(
            text = timestamp,
            color = if (isUnread) Green600 else Gray600,
            fontWeight = if (isUnread) FontWeight.Medium else FontWeight.Normal,
            fontSize = 12.sp
        )
    }
}