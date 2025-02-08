package com.umar.chat.ui.screens

object ScreenParams {
    const val JID = "jid"
}

sealed class Screens(val route: String) {
    data object Chat : Screens("chat")
    data object Messaging : Screens("messaging/{${ScreenParams.JID}}") {
        fun createRoute(jid: String) = "messaging/$jid"
    }
}