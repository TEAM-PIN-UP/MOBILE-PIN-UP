package com.pinup.placePinup

import PToastHost
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.pinup.placePinup.extentions.jsonToArg
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.addpinbuddy.AddPinBuddyRoute
import com.pinup.placePinup.ui.article.detail.ArticleDetailRoute
import com.pinup.placePinup.ui.component.LogoutDialog
import com.pinup.placePinup.ui.findAccount.changePassword.ChangePasswordRoute
import com.pinup.placePinup.ui.findAccount.findId.FindIdRoute
import com.pinup.placePinup.ui.findAccount.findPassword.FindPasswordEmailRoute
import com.pinup.placePinup.ui.login.compose.LoginRoute
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import com.pinup.placePinup.ui.main.compose.MainNavHost
import com.pinup.placePinup.ui.my.pinch.PintsRoute
import com.pinup.placePinup.ui.my.pinch.write.PinchWriteRoute
import com.pinup.placePinup.ui.my.pinch.detail.PinchDetailRoute
import com.pinup.placePinup.ui.my.scrap.ScrapRoute
import com.pinup.placePinup.ui.onboarding.OnboardingRoute
import com.pinup.placePinup.ui.onboarding.choiceSignup.ChoiceSignUpRoute
import com.pinup.placePinup.ui.pinbuddy.PinBuddyRoute
import com.pinup.placePinup.ui.pinlogDetail.PinlogDetailRoute
import com.pinup.placePinup.ui.placeDetail.PlaceDetailRoute
import com.pinup.placePinup.ui.profilesetting.ProfileSettingRoute
import com.pinup.placePinup.ui.reviewwrite.compose.WriteReviewNavHost
import com.pinup.placePinup.ui.reviewwrite.successWriteReview.WriteReviewDetailRoute
import com.pinup.placePinup.ui.setting.SettingNavHost
import com.pinup.placePinup.ui.signup.compose.SignUpRoute
import com.pinup.placePinup.ui.userprofile.UserProfileRoute
import com.pinup.placePinup.util.Const.NavKey.PINLOG_WRITE_RESULT
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import rememberToastState

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
    val toast = rememberToastState()

    fun moveMain() {
        navHostController.navigate(PinUpAppDestination.Main) {
            popUpTo(navHostController.graph.id) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(userId) {
        if(userId != -1){
            startAppViewModel.updateUserId(userId)
        }
    }

    MaterialTheme {
        Box{
            NavHost(
                startDestination = PinUpAppDestination.Onboarding,
                navController = navHostController,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                composable<PinUpAppDestination.Onboarding>{
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

                composable<PinUpAppDestination.ChoiceSignUp>{
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

                composable<PinUpAppDestination.Login>{
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

                composable<PinUpAppDestination.FindPasswordEmail>{
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

                composable<PinUpAppDestination.FindId>{
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

                composable<PinUpAppDestination.ChangePassword>{
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
                        userId = uiState.value.userId,
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
                        },
                        onMovePinBuddy = {
                            navHostController.navigate(PinUpAppDestination.PinBuddy) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        onProfileModifyClick = {
                            navHostController.navigate(PinUpAppDestination.ProfileSetting)
                        },
                        onMoveScrap = {
                            navHostController.navigate(PinUpAppDestination.Scrap) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        onMoveUserProfileWithName = {
                            navHostController.navigate(PinUpAppDestination.UserProfile(name = it))
                        },
                        onMoveUserProfileWithId = {
                            navHostController.navigate(PinUpAppDestination.UserProfile(memberId = it))
                        },
                        updateUserId = {
                            startAppViewModel.updateUserId(it)
                        },
                        onClickArticle = {
                            navHostController.navigate(PinUpAppDestination.ArticleDetail(it))
                        },
                        onMovePints = {
                            navHostController.navigate(PinUpAppDestination.Pints(it))
                        }
                    )
                }

                composable<PinUpAppDestination.WriteReview> {
                    WriteReviewNavHost(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onCompleteAndBackPressed = {
                            navHostController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(PINLOG_WRITE_RESULT, true)
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
                            navHostController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(PINLOG_WRITE_RESULT, true)
                            navHostController.popBackStack()
                        },
                        onClickPlaceDetail = {
                            navHostController.navigate(PinUpAppDestination.PlaceDetail(it))
                        }
                    )
                }

                composable<PinUpAppDestination.ArticleDetail> {
                    ArticleDetailRoute(
                        onClickBack = {
                            navHostController.popBackStack()
                        },
                        onClickPlaceDetail = {
                            navHostController.navigate(PinUpAppDestination.PlaceDetail(it)) {
                                restoreState = true
                            }
                        },
                        onClickArticle = {
                            navHostController.navigate(PinUpAppDestination.ArticleDetail(it)) {
                                restoreState = true
                            }
                        }
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
                        },
                        onMoveUserProfile = {
                            navHostController.navigate(PinUpAppDestination.UserProfile(name = it))
                        }
                    )
                }

                composable<PinUpAppDestination.Pints> {
                    PintsRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMovePintsDetail = {
                            navHostController.navigate(PinUpAppDestination.PinchDetail(it))
                        },
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

                composable<PinUpAppDestination.ProfileSetting> {
                    ProfileSettingRoute(
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
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
                        },
                        onChangedPassword = {
                            navHostController.navigate(PinUpAppDestination.ChangePassword)
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
                        navHostController = navHostController,
                        onBackPressed = {
                            navHostController.popBackStack()
                        },
                        onMoveWriteReview = {
                            navHostController.navigate(PinUpAppDestination.WriteReview(0, selectPlace = Json.encodeToString(it)))
                        },
                        onMovePintsDetail = {
                            navHostController.popBackStack()
                            navHostController.navigate(PinUpAppDestination.PinchDetail(it))
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
                        },
                        onClickSearch = {
                            navHostController.navigate(PinUpAppDestination.AddPinBuddy)
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
                        contextFactory = contextFactory,
                        onClickDetail = { navHostController.navigate(PinUpAppDestination.PinlogDetail(it)) },
                        onMovePinchWrite = { navHostController.navigate(PinUpAppDestination.PinchWrite(0)) },
                        onMovePintsDetail = { navHostController.navigate(PinUpAppDestination.PinchDetail(it)) },
                        onClickBack = {
                            navHostController.popBackStack()
                        },
                        onSettingClick = { navHostController.navigate(PinUpAppDestination.Setting) }
                    )
                }

                composable<PinUpAppDestination.Scrap> {
                    ScrapRoute(
                        onMovePlaceDetail = { navHostController.navigate(PinUpAppDestination.PlaceDetail(it)) },
                        onBackPressed = { navHostController.popBackStack() },
                        onClickGoFeed = { navHostController.navigate(PinUpAppDestination.WriteReview) }
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

            PToastHost(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 24.dp, start = 20.dp, end = 20.dp),
                state = toast
            )

            LaunchedEffect(uiState.value.errorMessage) {
                if(uiState.value.errorMessage.isNotEmpty()){
                    toast.show(uiState.value.errorMessage)
                }
            }
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
        val selectPlace: String? = null
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
        val memberId: Int = -1,
        val name: String = ""
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
    data class Pints(
        val memberId: Int
    ) : PinUpAppDestination

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
    @Serializable
    data object ProfileSetting : PinUpAppDestination
}