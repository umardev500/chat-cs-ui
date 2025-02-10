package com.umar.chat.ui.components.atoms

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.umar.chat.ui.theme.Green600

@Composable
fun Avatar(modifier: Modifier = Modifier, painter: Painter, isOnline: Boolean = false) {
    Box(
        modifier = modifier
            .size(50.dp)
    ) {
        Image(
            painter,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd)
                    .background(Green600, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}