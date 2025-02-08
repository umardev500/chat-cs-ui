package com.umar.chat.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun RegularText(
    text: String,
    color: Color,
    fontSize: Int = 14,
    style: TextStyle = TextStyle()
) {
    val mergedStyle = style.merge(TextStyle())

    Text(
        text,
        color = color,
        fontSize = fontSize.sp,
        style = mergedStyle
    )
}