package com.pinup.pinup

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinup.pinup.event.DetailPlaceEventBus
import com.pinup.pinup.extentions.jsonToArg
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.addpinbuddy.AddPinBuddyRoute
import com.pinup.pinup.ui.component.LogoutDialog
import com.pinup.pinup.ui.findAccount.findPassword.ChangePasswordRoute
import com.pinup.pinup.ui.findAccount.findPassword.FindPasswordEmailRoute
import com.pinup.pinup.ui.login.compose.LoginRoute
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.main.compose.MainNavHost
import com.pinup.pinup.ui.onboarding.OnboardingRoute
import com.pinup.pinup.ui.onboarding.choiceSignup.ChoiceSignUpRoute
import com.pinup.pinup.ui.pinbuddy.PinBuddyRoute
import com.pinup.pinup.ui.pinlogDetail.PinlogDetailRoute
import com.pinup.pinup.ui.reviewwrite.compose.WriteReviewNavHost
import com.pinup.pinup.ui.setting.SettingNavHost
import com.pinup.pinup.ui.signup.compose.SignUpRoute
import com.pinup.pinup.ui.userprofile.UserProfileRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun PinUpApp(
    contextFactory: ContextFactory,
    navHostController: NavHostController = rememberNavController(),
    startAppViewModel: StartAppViewModel = koinViewModel(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val uiState = startAppViewModel.uiState.collectAsStateWithLifecycle()
    val startDestination = when (uiState.value.isLogin) {
        true -> {
            PinUpAppDestination.Main
        }
        false -> {
            PinUpAppDestination.Onboarding
        }
        else -> {
            return
        }
    }

    fun moveMain() {
        navHostController.navigate(PinUpAppDestination.Main) {
            popUpTo(navHostController.graph.id) {
                inclusive = true
            }
        }
    }

    MaterialTheme {
        Box {
            NavHost(
                startDestination = startDestination,
                navController = navHostController
            ) {
                composable<PinUpAppDestination.Onboarding>(
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(200))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it })
                    }
                ){
                    OnboardingRoute(
                        onMoveSignUpOnboarding = {
                            navHostController.navigate(PinUpAppDestination.ChoiceSignUp)
                        },
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login)
                        }
                    )
                }

                composable<PinUpAppDestination.ChoiceSignUp>(
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(200))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it })
                    }
                ){
                    ChoiceSignUpRoute(
                        contextFactory = contextFactory,
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login)
                        },
                        onMoveSignUp = { snsUserInfo ->
                            val snsUserInfoString = Json.encodeToString(snsUserInfo)
                            navHostController.navigate(PinUpAppDestination.SignUp(snsUserInfoString))
                        }
                    )
                }

                composable<PinUpAppDestination.Login>(
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(200))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it })
                    }
                ){
                    LoginRoute(
                        contextFactory = contextFactory,
                        onMoveSignUp = {
                            val snsUserInfoString = Json.encodeToString(it)
                            navHostController.navigate(PinUpAppDestination.SignUp(snsUserInfoString))
                        },
                        onMoveMain = {
                            moveMain()
                        },
                        onMoveFindPassword = {
                            navHostController.navigate(PinUpAppDestination.FindPasswordEmail)
                        }
                    )
                }

                composable<PinUpAppDestination.FindPasswordEmail>(
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(200))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it })
                    }
                ){
                    FindPasswordEmailRoute(
                        onMoveChangePassword = {
                            navHostController.navigate(PinUpAppDestination.ChangePassword)
                        },
                        onBackPressed = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable<PinUpAppDestination.ChangePassword>(
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(200))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it })
                    }
                ){
                    ChangePasswordRoute(
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login)
                        },
                        onBackPressed = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable<PinUpAppDestination.SignUp>(
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
                    val snsType = it.jsonToArg<SNSUserInfo>("snsUserInfo")?.snsType ?: SNSType.KAKAO
                    SignUpRoute(
                        snsType = snsType,
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveMain = {
                            moveMain()
                        }
                    )
                }

                composable<PinUpAppDestination.Main> {
                    MainNavHost(
                        onMoveWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it))
                        },
                        onMovePinlogDetail = {
                            navHostController.navigate(PinUpAppDestination.PinlogDetail(it))
                        },
                        onMoveAddPinBuddy = {
                            navHostController.navigate(PinUpAppDestination.AddPinBuddy)
                        },
                        onMovePinBuddy = {
                            navHostController.navigate(PinUpAppDestination.PinBuddy)
                        },
                        onMoveSetting = {
                            navHostController.navigate(PinUpAppDestination.Setting)
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it))
                        }
                    )
                }

                composable<PinUpAppDestination.WriteReview> {
                    WriteReviewNavHost(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveDetailPlace = {
                            navHostController.navigate(PinUpAppDestination.PinlogDetail(it))
                        }
                    )
                }

                composable<PinUpAppDestination.PinlogDetail> {
                    PinlogDetailRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it))
                        }
                    )
                }

                composable<PinUpAppDestination.AddPinBuddy> {
                    AddPinBuddyRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveUserProfile = {
                            navHostController.navigate(PinUpAppDestination.UserProfile(it))
                        }
                    )
                }

                composable<PinUpAppDestination.UserProfile> {
                    UserProfileRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable<PinUpAppDestination.PinBuddy> {
                    PinBuddyRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveUserProfile = {
                            navHostController.navigate(PinUpAppDestination.UserProfile(it))
                        }
                    )
                }

                composable<PinUpAppDestination.Setting> {
                    SettingNavHost(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveLoginScreen = {
                            navHostController.navigate(PinUpAppDestination.Login) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

            LogoutDialog(
                alertState = uiState.value.alertState,
                onLogoutClick = {
                    navHostController.navigate(PinUpAppDestination.Login) {
                        popUpTo(navHostController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onDisMissRequest = startAppViewModel::dismissAlert
            )
        }
    }
}


sealed interface PinUpAppDestination {
    @Serializable
    data object Onboarding : PinUpAppDestination
    @Serializable
    data object ChoiceSignUp : PinUpAppDestination
    @Serializable
    data object Main : PinUpAppDestination {
        @Serializable
        data class DetailPlace(val kakaoPlaceId: String) : PinUpAppDestination
    }
    @Serializable
    data class WriteReview(
        val reviewId: Int,
    ) : PinUpAppDestination
    @Serializable
    data class PinlogDetail(
        val reviewId: Int,
    ) : PinUpAppDestination
    @Serializable
    data object AddPinBuddy : PinUpAppDestination
    @Serializable
    data class UserProfile(
        val memberId: Int,
    ) : PinUpAppDestination
    @Serializable
    data object PinBuddy : PinUpAppDestination
    @Serializable
    data object Setting : PinUpAppDestination
    @Serializable
    data object Login : PinUpAppDestination
    @Serializable
    data object FindPasswordEmail : PinUpAppDestination
    @Serializable
    data object ChangePassword : PinUpAppDestination
    @Serializable
    data class SignUp(
        val snsUserInfo: String
    ) : PinUpAppDestination
}