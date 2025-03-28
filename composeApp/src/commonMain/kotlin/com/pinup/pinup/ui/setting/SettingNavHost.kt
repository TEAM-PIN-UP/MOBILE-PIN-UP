package com.pinup.pinup.ui.setting

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun SettingNavHost(
    onBackPressed: () -> Unit,
    onMoveLoginScreen: () -> Unit,
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = SettingDestination.Setting,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it })
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        }
    ) {
        composable<SettingDestination.Setting> {
            SettingRoute(
                onBackPressed = onBackPressed,
                onMoveLoginScreen = onMoveLoginScreen
            )
        }
    }
}

sealed interface SettingDestination {
    @Serializable
    data object Setting : SettingDestination
}