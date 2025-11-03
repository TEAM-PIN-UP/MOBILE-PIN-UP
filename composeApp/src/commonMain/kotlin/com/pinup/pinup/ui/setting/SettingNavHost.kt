package com.pinup.pinup.ui.setting

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.ui.profilesetting.ProfileSettingRoute
import com.pinup.pinup.ui.setting.complete.UnRegisterCompleteScreen
import com.pinup.pinup.ui.setting.unregister.UnRegisterRoute
import kotlinx.serialization.Serializable

@Composable
fun SettingNavHost(
    onBackPressed: () -> Unit,
    onMoveLoginScreen: () -> Unit,
    onMoveLoginOnboardingScreen: () -> Unit,
    onChangedPassword: () -> Unit = {},
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = SettingDestination.Setting,
    ) {
        composable<SettingDestination.Setting> {
            SettingRoute(
                onBackPressed = onBackPressed,
                onMoveLoginScreen = onMoveLoginScreen,
                onMoveUnRegister = {
                    navHostController.navigate(SettingDestination.UnRegister)
                },
                onChangedPassword = {
                    onChangedPassword()
                }
            )
        }

        composable<SettingDestination.UnRegister> {
            UnRegisterRoute(
                onBackPressed = {
                    navHostController.popBackStack()
                },
                onMoveCompleteUnRegister = {
                    navHostController.navigate(SettingDestination.Complete) {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<SettingDestination.Complete> {
            UnRegisterCompleteScreen(
                onMoveOnboarding = onMoveLoginOnboardingScreen
            )
        }
    }
}

sealed interface SettingDestination {
    @Serializable
    data object Setting : SettingDestination
    @Serializable
    data object UnRegister : SettingDestination
    @Serializable
    data object Complete : SettingDestination
}