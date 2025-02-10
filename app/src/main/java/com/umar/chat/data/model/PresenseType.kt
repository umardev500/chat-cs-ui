package com.umar.chat.data.model

sealed class PresenseType(val value: String) {
    data object Composing : PresenseType("composing")
    data object Unavailable : PresenseType("unavailable")
}