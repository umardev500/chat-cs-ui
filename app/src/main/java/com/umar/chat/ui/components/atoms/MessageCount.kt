package com.umar.chat.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.chat.ui.theme.Green600

@Composable
fun MessageCount(
    modifier: Modifier = Modifier,
    count: Int,
    color: Color = Green600,
) {
    Box(
        modifier = modifier
            .width(22.dp)
            .height(22.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$count",
            color = Color.White,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}