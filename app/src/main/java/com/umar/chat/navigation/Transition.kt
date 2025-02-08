package com.umar.chat.navigation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object SlideTransition {
    private const val DURATION = 350
    private val EASING: Easing = FastOutSlowInEasing

    val enter = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = DURATION, easing = EASING)
    )

    val exit = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -(fullWidth / 5) },
        animationSpec = tween(durationMillis = DURATION, easing = EASING)
    )

    val popEnter = slideInHorizontally(
        initialOffsetX = { fullWidth -> -(fullWidth / 5) },
        animationSpec = tween(durationMillis = DURATION, easing = EASING)
    )

    val popExit = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = DURATION, easing = EASING)
    )
}