package com.pinup.placePinup

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.pinup.placePinup.event.DetailPlaceEventBus
import com.pinup.placePinup.extentions.jsonToArg
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.addpinbuddy.AddPinBuddyRoute
import com.pinup.placePinup.ui.article.detail.ArticleDetailRoute
import com.pinup.placePinup.ui.findAccount.changePassword.ChangePasswordRoute
import com.pinup.placePinup.ui.findAccount.findId.FindIdRoute
import com.pinup.placePinup.ui.findAccount.findPassword.FindPasswordEmailRoute
import com.pinup.placePinup.ui.image.DetailImageRoute
import com.pinup.placePinup.ui.login.compose.LoginRoute
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import com.pinup.placePinup.ui.main.compose.MainNavHost
import com.pinup.placePinup.ui.my.pinch.PintsRoute
import com.pinup.placePinup.ui.my.pinch.detail.PinchDetailRoute
import com.pinup.placePinup.ui.my.pinch.write.PinchWriteRoute
import com.pinup.placePinup.ui.my.scrap.ScrapRoute
import com.pinup.placePinup.ui.notification.NotificationRoute
import com.pinup.placePinup.ui.onboarding.OnboardingRoute
import com.pinup.placePinup.ui.onboarding.choiceSignup.ChoiceSignUpRoute
import com.pinup.placePinup.ui.pinbuddy.PinBuddyRoute
import com.pinup.placePinup.ui.pinlogDetail.PinlogDetailRoute
import com.pinup.placePinup.ui.placeDetail.PlaceDetailRoute
import com.pinup.placePinup.ui.profilesetting.ProfileSettingRoute
import com.pinup.placePinup.ui.report.ReportRoute
import com.pinup.placePinup.ui.reviewwrite.compose.WriteReviewNavHost
import com.pinup.placePinup.ui.reviewwrite.successWriteReview.WriteReviewDetailRoute
import com.pinup.placePinup.ui.setting.SettingNavHost
import com.pinup.placePinup.ui.signup.compose.SignUpRoute
import com.pinup.placePinup.ui.userprofile.UserProfileRoute
import com.pinup.placePinup.util.Const.NavKey.PINLOG_WRITE_RESULT
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun PinUpNavHost(
    userId: Int,
    navHostController: NavHostController,
    contextFactory: ContextFactory,
    onUpdateUserId: (Int) -> Unit,
    onMoveMain: () -> Unit,
) {
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
                    navHostController.navigate(PinUpAppDestination.Main(), navOptions)
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
                    navHostController.navigate(PinUpAppDestination.Main())
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
                onMoveMain = onMoveMain,
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
                onMoveMain = onMoveMain
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
                updateUserId = onUpdateUserId,
                onMoveDetailImage = { position, images ->
                    navHostController.navigate(PinUpAppDestination.DetailImage(position, images))
                },
                onClickArticle = {
                    navHostController.navigate(PinUpAppDestination.ArticleDetail(it))
                },
                onMovePints = {
                    navHostController.navigate(PinUpAppDestination.Pints(it))
                },
                onMoveNotification = {
                    navHostController.navigate(PinUpAppDestination.Notification)
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
                onMovePlaceDetail = { kakaoPlaceId ->
                    // 장소 상세가 아니라 지도로 이동해서 해당 장소를 센터링한다.
                    DetailPlaceEventBus.requestFocusPlace(kakaoPlaceId)
                    navHostController.navigate(PinUpAppDestination.Main()) {
                        launchSingleTop = true
                        popUpTo<PinUpAppDestination.Main> {
                            inclusive = false
                        }
                    }
                },
                onMoveUserProfile = {
                    navHostController.navigate(PinUpAppDestination.UserProfile(name = it))
                },
                onMoveDetailImage = { position, images ->
                    navHostController.navigate(PinUpAppDestination.DetailImage(position, images))
                },
                onMoveReport = { targetId, reportType ->
                    navHostController.navigate(PinUpAppDestination.Report(targetId, reportType.name))
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
                onMoveUserProfile = { memberId, friendRequestId ->
                    navHostController.navigate(PinUpAppDestination.UserProfile(memberId = memberId, friendRequestId = friendRequestId))
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
                onMovePints = { navHostController.navigate(PinUpAppDestination.Pints(it)) },
                onMovePintsDetail = { navHostController.navigate(PinUpAppDestination.PinchDetail(it)) },
                onClickBack = {
                    navHostController.popBackStack()
                },
                onMoveReport = { id, type ->
                    navHostController.navigate(PinUpAppDestination.Report(id, type.name))
                },
                onMoveMap = {
                    // 뱃지 탭 시 VM 이 requestFocusPlace 로 focusPlace 를 세팅해둔 상태.
                    // Main 위 화면들만 pop 하고 Main 으로 복귀하면, MainNavHost 의 focusPlace 관찰자가 Map 탭으로 전환한다.
                    navHostController.navigate(PinUpAppDestination.Main()) {
                        popUpTo<PinUpAppDestination.Main> { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<PinUpAppDestination.Scrap> {
            ScrapRoute(
                onMovePlaceDetail = { navHostController.navigate(PinUpAppDestination.PlaceDetail(it)) },
                onBackPressed = { navHostController.popBackStack() },
                onClickGoFeed = { navHostController.navigate(PinUpAppDestination.WriteReview) }
            )
        }

        composable<PinUpAppDestination.DetailImage> {
            DetailImageRoute(
                onClose = { navHostController.popBackStack() }
            )
        }

        composable<PinUpAppDestination.Report> {
            ReportRoute(
                onBackPressed = { navHostController.popBackStack() }
            )
        }

        composable<PinUpAppDestination.Notification> {
            NotificationRoute(
                onBackPressed = { navHostController.popBackStack() },
                onMovePlaceDetail = { placeId ->
                    navHostController.navigate(PinUpAppDestination.PlaceDetail(placeId))
                },
                onMovePinlogDetail = { id ->
                    navHostController.navigate(PinUpAppDestination.PinlogDetail(id))
                },
                onMovePinBuddy = {
                    navHostController.navigate(PinUpAppDestination.PinBuddy)
                },
                onMoveUserProfileWithId = { id ->
                    navHostController.navigate(PinUpAppDestination.UserProfile(id))
                },
                onMoveMyProfile = {
                    navHostController.navigate(PinUpAppDestination.Main(true))
                }
            )
        }
    }
}
