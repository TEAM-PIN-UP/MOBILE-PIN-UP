package com.pinup.pinup

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.pinup.pinup.extentions.jsonToArg
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.ui.addpinbuddy.AddPinBuddyRoute
import com.pinup.pinup.ui.article.detail.ArticleDetailRoute
import com.pinup.pinup.ui.component.LogoutDialog
import com.pinup.pinup.ui.findAccount.changePassword.ChangePasswordRoute
import com.pinup.pinup.ui.findAccount.findId.FindIdRoute
import com.pinup.pinup.ui.findAccount.findPassword.FindPasswordEmailRoute
import com.pinup.pinup.ui.login.compose.LoginRoute
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.main.compose.MainNavHost
import com.pinup.pinup.ui.my.pinch.PinchWriteRoute
import com.pinup.pinup.ui.my.pinch.detail.PinchDetailRoute
import com.pinup.pinup.ui.onboarding.OnboardingRoute
import com.pinup.pinup.ui.onboarding.choiceSignup.ChoiceSignUpRoute
import com.pinup.pinup.ui.pinbuddy.PinBuddyRoute
import com.pinup.pinup.ui.pinlogDetail.PinlogDetailRoute
import com.pinup.pinup.ui.placeDetail.PlaceDetailRoute
import com.pinup.pinup.ui.reviewwrite.compose.WriteReviewNavHost
import com.pinup.pinup.ui.reviewwrite.successWriteReview.WriteReviewDetailRoute
import com.pinup.pinup.ui.setting.SettingNavHost
import com.pinup.pinup.ui.signup.compose.SignUpRoute
import com.pinup.pinup.ui.userprofile.UserProfileRoute
import kotlinx.coroutines.CoroutineScope
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
    scope: CoroutineScope = rememberCoroutineScope(),
    userId: Int = -1
) {
    val uiState = startAppViewModel.uiState.collectAsStateWithLifecycle()

    fun moveMain() {
        navHostController.navigate(PinUpAppDestination.Main) {
            popUpTo(navHostController.graph.id) {
                inclusive = true
            }
        }
    }

    MaterialTheme {
        Box{
            NavHost(
                startDestination = PinUpAppDestination.Onboarding,
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
                    val navOptions = navOptions {
                        popUpTo<PinUpAppDestination.Onboarding> {
                            inclusive = true
                        }
                    }
                    OnboardingRoute(
                        onMoveSignUpOnboarding = {
                            navHostController.navigate(PinUpAppDestination.ChoiceSignUp, navOptions)
                        },
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login, navOptions)
                        },
                        onMoveMain = {
                            navHostController.navigate(PinUpAppDestination.Main, navOptions)
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
                        },
                        onMoveMain = {
                            navHostController.navigate(PinUpAppDestination.Main)
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
                        },
                        onMoveFindId = {
                            navHostController.navigate(PinUpAppDestination.FindId)
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
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                    )
                }

                composable<PinUpAppDestination.FindId>(
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
                    FindIdRoute(
                        onMoveLogin = {
                            navHostController.navigate(PinUpAppDestination.Login) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        onMoveFindPassword = {
                            navHostController.navigate(PinUpAppDestination.FindPasswordEmail)
                        },
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
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
                            navHostController.navigate(PinUpAppDestination.Login) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
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
                        contextFactory = contextFactory,
                        userId = userId,
                        onMoveWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it, null))
                        },
                        onMovePinlogDetail = {
                            navHostController.navigate(PinUpAppDestination.PinlogDetail(it))
                        },
                        onMoveSetting = {
                            navHostController.navigate(PinUpAppDestination.Setting)
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it, null))
                        },
                        onMoveNewWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(0, it))
                        },
                        onClickArticleDetail = {
                            navHostController.navigate(PinUpAppDestination.ArticleDetail(it))
                        },
                        onMovePlaceDetail = {
                            navHostController.navigate(PinUpAppDestination.PlaceDetail(it))
                        },
                        onMovePinchWrite = {
                            navHostController.navigate(PinUpAppDestination.PinchWrite(0))
                        },
                        onMovePintsDetail = {
                            navHostController.navigate(PinUpAppDestination.PinchDetail(it))
                        }
                    )
                }

                composable<PinUpAppDestination.ArticleDetail> {
                    ArticleDetailRoute(
                        onClickBack = {
                            navHostController.popBackStack()
                        },
                        onClickPlaceDetail = {

                        },
                    )
                }

                composable<PinUpAppDestination.WriteReview> {
                    WriteReviewNavHost(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveDetailPlace = {
                            navHostController.popBackStack()
                            navHostController.navigate(PinUpAppDestination.WriteReviewDetail(it))
                        }
                    )
                }

                composable<PinUpAppDestination.WriteReviewDetail> {
                    WriteReviewDetailRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                    )
                }

                composable<PinUpAppDestination.PinlogDetail> {
                    PinlogDetailRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it, null))
                        },
                        onMovePlaceDetail = {
                            navHostController.navigate(PinUpAppDestination.PlaceDetail(it))
                        }
                    )
                }

                composable<PinUpAppDestination.PinchDetail> {
                    PinchDetailRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.PinchWrite(it))
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
                        },
                        onMoveLoginOnboardingScreen = {
                            navHostController.navigate(PinUpAppDestination.Onboarding) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable<PinUpAppDestination.PlaceDetail> {
                    PlaceDetailRoute(
                        onClickBack = {
                            navHostController.popBackStack()
                        },
                        onClickEdit = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(it, null))
                        },
                        onMovePinlogDetail = {
                            navHostController.navigate(PinUpAppDestination.PinlogDetail(it))
                        },
                        onMoveWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(0, it))
                        },
                    )
                }

                composable<PinUpAppDestination.PinchWrite> {
                    PinchWriteRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(0, it))
                        },
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
        val reviewId: Int?,
        val placeId: String?
    ) : PinUpAppDestination
    @Serializable
    data class WriteReviewDetail(
        val reviewId: Int,
    ) : PinUpAppDestination
    @Serializable
    data class PinlogDetail(
        val reviewId: Int,
    ) : PinUpAppDestination
    @Serializable
    data class ArticleDetail(
        val pintsId: Int,
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
    data object FindId : PinUpAppDestination
    @Serializable
    data object ChangePassword : PinUpAppDestination
    @Serializable
    data class SignUp(
        val snsUserInfo: String
    ) : PinUpAppDestination

    @Serializable
    data object Scrap : PinUpAppDestination
    @Serializable
    data class PinchWrite(
        val pintsId: Int
    ) : PinUpAppDestination
    @Serializable
    data class PinchDetail(
        val pintsId: Int
    ) : PinUpAppDestination
    @Serializable
    data class PlaceDetail(
        val kakaoPlaceId: String
    ) : PinUpAppDestination
}