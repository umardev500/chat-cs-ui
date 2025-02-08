package com.umar.chat.navigation

import androidx.compose.runtime.staticCompositionLocalOf

data class NavigationActions(
    val navigateToMessaging: (jid: String) -> Unit,
    val backStack: () -> Unit
)

val LocalNavigationActions = staticCompositionLocalOf<NavigationActions> {
    error("No navigation actions provided")
}