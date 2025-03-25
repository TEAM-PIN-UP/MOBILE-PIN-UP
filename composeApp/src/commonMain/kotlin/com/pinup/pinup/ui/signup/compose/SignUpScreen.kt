package com.pinup.pinup.ui.signup.compose

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.signup.NickNameState
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.signup.TermsOfServiceState
import com.pinup.pinup.ui.theme.Colors
import kotlinx.serialization.Serializable

@Composable
fun SignUpScreen(
    navHostController: NavHostController,
    nicknameState: NickNameState,
    profileUrl: ByteArray,
    termsOfServiceState: TermsOfServiceState,
    onValueChange: (String) -> Unit,
    onProfileImageChange: (ByteArray) -> Unit,
    onAllAgreeClick: (TermsOfService) -> Unit,
    onUsingServiceAgreeClick: (TermsOfService) -> Unit,
    onCollectDataAgreeClick: (TermsOfService) -> Unit,
    onCollectLocationAgreeClick: (TermsOfService) -> Unit,
    onMarketingAgreeClick: (TermsOfService) -> Unit,
    onSignUpClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Colors.White)
    ) {
        TitleBar(
            title = "회원가입",
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

        NavHost(
            navController = navHostController,
            startDestination = SignUpDestination.Name,
        ) {
            composable<SignUpDestination.Name>(
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
                InputNameScreen(
                    nickname = nicknameState.nickname,
                    isNicknameUsed = nicknameState.isNicknameUsed,
                    onValueChange = onValueChange,
                    onMoveSelectProfileImage = {
                        navHostController.navigate(SignUpDestination.Image)
                    }
                )
            }

            composable<SignUpDestination.Image>(
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
                SelectProfileImageScreen(
                    profileImage = profileUrl,
                    nickname = nicknameState.nickname,
                    onUpdateProfileImage = onProfileImageChange,
                    onMoveTermsOfService = {
                        navHostController.navigate(SignUpDestination.Terms)
                    },
                )
            }

            composable<SignUpDestination.Terms>(
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
                TermsOfServiceRoute(
                    onAllAgreeClick = onAllAgreeClick,
                    onUsingServiceAgreeClick = onUsingServiceAgreeClick,
                    onCollectDataAgreeClick = onCollectDataAgreeClick,
                    onCollectLocationAgreeClick = onCollectLocationAgreeClick,
                    onMoveComplete = {
                        navHostController.navigate(SignUpDestination.Complete)
                    },
                    onMarketingAgreeClick = onMarketingAgreeClick,
                    termsOfServiceState = termsOfServiceState
                )
            }

            composable<SignUpDestination.Complete>(
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
                CompleteScreen(
                    onSignUpClick = onSignUpClick
                )
            }
        }
    }
}

sealed interface SignUpDestination {
    @Serializable
    data object Name : SignUpDestination
    @Serializable
    data object Image : SignUpDestination
    @Serializable
    data object Terms : SignUpDestination
    @Serializable
    data object Complete : SignUpDestination
}