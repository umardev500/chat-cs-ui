package com.umar.chat.data.model

sealed class StatusType(val value: String) {
    data object Online : StatusType("online")
    data object Offline : StatusType("offline")
}