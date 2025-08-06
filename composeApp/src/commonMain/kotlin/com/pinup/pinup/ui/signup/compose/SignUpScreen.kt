package com.pinup.pinup.ui.signup.compose

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.signup.EmailState
import com.pinup.pinup.ui.signup.NickNameState
import com.pinup.pinup.ui.signup.PasswordState
import com.pinup.pinup.ui.signup.TermsOfService
import com.pinup.pinup.ui.signup.TermsOfServiceState
import com.pinup.pinup.ui.theme.Colors
import kotlinx.serialization.Serializable

@Composable
fun SignUpScreen(
    navHostController: NavHostController,
    snsType: SNSType,
    emailState: EmailState,
    passwordState : PasswordState,
    nicknameState: NickNameState,
    profileUrl: ByteArray,
    termsOfServiceState: TermsOfServiceState,
    onEmailChanged : (String) -> Unit,
    onCodeChanged : (String) -> Unit,
    onClickVerify : () -> Unit,
    onPasswordChanged : (String) -> Unit,
    onPasswordAgainChanged : (String) -> Unit,
    onClickShowPassword : () -> Unit,
    onValueChange: (String) -> Unit,
    onProfileImageChange: (ByteArray) -> Unit,
    onTermAgreeClick: (TermsOfService) -> Unit,
    onSignUpClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Colors.White)
    ) {
        NavHost(
            navController = navHostController,
            startDestination = if(snsType == SNSType.EMAIL) SignUpDestination.Image else SignUpDestination.Image,
        ) {
            composable<SignUpDestination.InputEmail>(
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
                InputEmailScreen(
                    emailState = emailState,
                    onEmailChanged = onEmailChanged,
                    onCodeChanged = onCodeChanged,
                    onClickVerify = onClickVerify,
                    onMovePassword = { navHostController.navigate(SignUpDestination.InputPassword) },
                    onBackPressed = onBackPressed
                )
            }

            composable<SignUpDestination.InputPassword>(
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
                InputPasswordScreen(
                    passwordState = passwordState,
                    onPasswordChanged = onPasswordChanged,
                    onPasswordAgainChanged = onPasswordAgainChanged,
                    onClickShowPassword = onClickShowPassword,
                    onClickConfirm = { navHostController.navigate(SignUpDestination.Terms) },
                    onBackPressed = onBackPressed
                )
            }

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
                    },
                    onBackPressed = onBackPressed,
                    isPassNickname = nicknameState.isPassNickname
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
                    onClickSignup = onSignUpClick,
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
                TermsOfServiceScreen(
                    onTermAgreeClick = onTermAgreeClick,
                    onMoveInputName = {
                        navHostController.navigate(SignUpDestination.Name)
                    },
                    isAllAgree = termsOfServiceState.isAllAgree,
                    isUsingServiceAgree = termsOfServiceState.isUsingServiceAgree,
                    isCollectDataAgree = termsOfServiceState.isCollectDataAgree,
                    isCollectLocationAgree = termsOfServiceState.isCollectLocationAgree,
                    isMarketingAgreeClick = termsOfServiceState.isMarketingAgreeAgree,
                    isPassValidation = termsOfServiceState.isPassValidation,
                    onBackPressed = onBackPressed,
                    onClickDetailTerm = { url ->
                        navHostController.navigate(SignUpDestination.TermDetail(url))
                    }
                )
            }

            composable<SignUpDestination.TermDetail>(
                enterTransition = {
                    slideInVertically(initialOffsetY = { it })
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutVertically(targetOffsetY = { it })
                }
            ) {
                val url = it.arguments?.get("url").toString()
                DetailTermScreen(
                    onBackPressed = {
                        navHostController.popBackStack()
                    },
                    url = url,
                )
            }
        }
    }
}

sealed interface SignUpDestination {
    @Serializable
    data object InputEmail : SignUpDestination
    @Serializable
    data object InputPassword : SignUpDestination
    @Serializable
    data object Terms : SignUpDestination
    @Serializable
    data class TermDetail(
        val url : String
    ) : SignUpDestination
    @Serializable
    data object Name : SignUpDestination
    @Serializable
    data object Image : SignUpDestination
    @Serializable
    data object Complete : SignUpDestination
}