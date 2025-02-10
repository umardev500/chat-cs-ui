package com.umar.chat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umar.chat.ui.screens.ChatScreen
import com.umar.chat.ui.screens.MessagingScreen
import com.umar.chat.ui.screens.Screens

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    val navigationActions = remember {
        NavigationActions(
            navigateToMessaging = { jid ->
                navController.navigate(Screens.Messaging.createRoute(jid))
            },
            backStack = {
                navController.popBackStack()
            }
        )
    }

    NavigationLocalComp.Provide(
        navigationActions = navigationActions
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.Chat.route
        ) {
            composable(
                route = Screens.Chat.route,
                enterTransition = { SlideTransition.enter },
                exitTransition = { SlideTransition.exit },
                popEnterTransition = { SlideTransition.popEnter },
                popExitTransition = { SlideTransition.popExit }
            ) {
                ChatScreen()
            }
            composable(
                route = Screens.Messaging.route,
                enterTransition = { SlideTransition.enter },
                exitTransition = { SlideTransition.exit },
                popEnterTransition = { SlideTransition.popEnter },
                popExitTransition = { SlideTransition.popExit }
            ) {
                MessagingScreen()
            }

        }
    }

}