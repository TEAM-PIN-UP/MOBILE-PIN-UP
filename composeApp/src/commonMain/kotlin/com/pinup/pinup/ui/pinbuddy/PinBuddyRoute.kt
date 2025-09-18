package com.pinup.pinup.ui.pinbuddy

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.theme.Texts
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import rememberToastState

@Composable
fun PinBuddyRoute(
    onBackPressed: () -> Unit,
    onMoveUserProfile: (Int) -> Unit,
    onClickSearch: () -> Unit = {},
    pinBuddyViewModel: PinBuddyViewModel = koinViewModel()
) {
    val uiState = pinBuddyViewModel.uiState.collectAsStateWithLifecycle()
    val toast = rememberToastState()

    LaunchedEffect(Unit) {
        pinBuddyViewModel.uiEvent.collectLatest {
            when(it) {
                PinBuddyUiEvent.SuccessAccept -> {
                    toast.show(Texts.Toast.ACCEPT_PIN_BUDDY)
                }
                PinBuddyUiEvent.SuccessCancel -> {
                    toast.show(Texts.Toast.CANCEL_PIN_BUDDY_REQEUST)
                }
                PinBuddyUiEvent.SuccessDelete -> {
                    toast.show(Texts.Toast.DELETE_PIN_BUDDY)
                }
                PinBuddyUiEvent.SuccessRefuse -> {
                    toast.show(Texts.Toast.REFUSE_PIN_BUDDY)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        pinBuddyViewModel.initPinBuddies()
    }

    PToastHost(state = toast)

    PinBuddyScreen(
        pinBuddies = uiState.value.pinBuddies.toPersistentList(),
        sentPinBuddyRequests = uiState.value.sentPinBuddyRequests.toPersistentList(),
        receivePinBuddyRequests = uiState.value.receivePinBuddyRequests.toPersistentList(),
        onBackPressed = onBackPressed,
        onDeletePinBuddy = pinBuddyViewModel::deletePinBuddy,
        onDeletePinBuddyRequest = pinBuddyViewModel::deletePinBuddyRequest,
        onProfileClick = onMoveUserProfile,
        onAcceptClick = pinBuddyViewModel::acceptPinBuddy,
        onRejectClick = pinBuddyViewModel::rejectPinBuddy,
        onClickSearch = onClickSearch
    )
}