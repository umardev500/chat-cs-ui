package com.umar.chat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.umar.chat.repository.WebsocketRepository

val LocalWebsocketRepository = staticCompositionLocalOf<WebsocketRepository> {
    error("No navigation actions provided")
}

object NavigationLocalComp {
    @Composable
    fun Provide(
        navigationActions: NavigationActions,
        content: @Composable () -> Unit
    ) {
        CompositionLocalProvider(
            LocalNavigationActions provides navigationActions,
        ) {
            content()
        }
    }
}