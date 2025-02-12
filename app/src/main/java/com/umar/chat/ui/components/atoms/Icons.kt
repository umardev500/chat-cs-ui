package com.umar.chat.ui.components.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.umar.chat.ui.theme.Gray800
import com.umar.chat.ui.theme.fontMsRoundedFamily
import com.umar.chat.ui.theme.fontMsRoundedFillFamily

enum class IconType(val unicode: String, val type: String) {
    HOME("\uE88A", "navigation"),
    SEARCH("\uE8B6", "search"),
    LOW_PRIORITY("\uE16D", "priority"),
    MORE_VERT("\uE5D4", "more"),
    MORE_HORIZ("\uE5D3", "more"),
    SETTINGS("\uE8B8", "settings"),
    ARROW_BACK("\uE5C4", "arrow");
}


@Composable
fun MsIconRounded(icon: IconType, size: Int = 24, color: Color = Gray800) {
    Text(
        text = icon.unicode,
        fontFamily = fontMsRoundedFamily,
        style = TextStyle(
            fontSize = size.sp,
            color = color
        )
    )
}

@Composable
fun MsIconRoundedFill(icon: IconType, size: Int = 24, color: Color = Gray800) {
    Text(
        text = icon.unicode,
        fontFamily = fontMsRoundedFillFamily,
        style = TextStyle(
            fontSize = size.sp,
            color = color
        )
    )
}
