package com.pinup.placePinup

import PToastHost
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
import androidx.navigation.compose.rememberNavController
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.component.ForcedCheckUpdateDialog
import com.pinup.placePinup.ui.component.LogoutDialog
import com.pinup.placePinup.ui.component.OptionalCheckUpdateDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable
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
        navHostController.navigate(PinUpAppDestination.Main()) {
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
            PinUpNavHost(
                userId = uiState.value.userId,
                navHostController = navHostController,
                contextFactory = contextFactory,
                onUpdateUserId = {
                    startAppViewModel.updateUserId(it)
                },
                onMoveMain = {
                    moveMain()
                },
            )

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

            when(val updateDialogState = uiState.value.updateDialogState) {
                UpdateDialogState.Loading -> {
                    // TODO 로딩UI
                }

                is UpdateDialogState.UpdateRequired -> {
                    when(updateDialogState.type) {
                        UpdateType.FORCE -> {
                            ForcedCheckUpdateDialog(
                                title = updateDialogState.message.title,
                                description = updateDialogState.message.body,
                                onConfirm = {
                                    // TODO 업데이트 이동
                                },
                            )
                        }
                        UpdateType.OPTIONAL -> {
                            OptionalCheckUpdateDialog(
                                title = updateDialogState.message.title,
                                description = updateDialogState.message.body,
                                onConfirm = {
                                    // TODO 업데이트 이동
                                },
                                onDismiss = {
                                    startAppViewModel.dismissUpdateDialog()
                                },
                                onRemindLater = {
                                    startAppViewModel.remindLaterUpdateDialog()
                                }
                            )
                        }
                    }
                }

                else -> Unit
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
    data class Main(
        val isMyPage: Boolean = false,
    ) : PinUpAppDestination
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
        val friendRequestId: Int = -1,
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

    @Serializable
    data class DetailImage(
        val position: Int,
        val images: List<String>
    ) : PinUpAppDestination

    @Serializable
    data class Report(
        val targetId: Int,
        val reportType: String
    ) : PinUpAppDestination

    @Serializable
    data object Notification: PinUpAppDestination
}
