package com.pinup.placePinup.ui.pinbuddy

import PToastHost
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import rememberToastState

@Composable
fun PinBuddyRoute(
    onBackPressed: () -> Unit,
    onMoveUserProfile: (Int, Int) -> Unit,
    onClickSearch: () -> Unit = {},
    pinBuddyViewModel: PinBuddyViewModel = koinViewModel()
) {
    val uiState = pinBuddyViewModel.uiState.collectAsStateWithLifecycle()
    val toast = rememberToastState()
    val acceptText = stringResource(Res.string.toast_accept_pin_buddy)
    val cancelText = stringResource(Res.string.toast_cancel_pin_buddy_request)
    val deleteText = stringResource(Res.string.toast_delete_pin_buddy)
    val refuseText = stringResource(Res.string.toast_refuse_pin_buddy)

    LaunchedEffect(Unit) {
        pinBuddyViewModel.uiEvent.collectLatest {
            when(it) {
                PinBuddyUiEvent.SuccessAccept -> {
                    toast.show(acceptText)
                }
                PinBuddyUiEvent.SuccessCancel -> {
                    toast.show(cancelText)
                }
                PinBuddyUiEvent.SuccessDelete -> {
                    toast.show(deleteText)
                }
                PinBuddyUiEvent.SuccessRefuse -> {
                    toast.show(refuseText)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        pinBuddyViewModel.initPinBuddies()
    }

    PToastHost(state = toast)

    PinBuddyScreen(
        pinBuddies = uiState.value.pinBuddies,
        sentPinBuddyRequests = uiState.value.sentPinBuddyRequests,
        receivePinBuddyRequests = uiState.value.receivePinBuddyRequests,
        onBackPressed = onBackPressed,
        onDeletePinBuddy = pinBuddyViewModel::deletePinBuddy,
        onDeletePinBuddyRequest = pinBuddyViewModel::deletePinBuddyRequest,
        onProfileClick = onMoveUserProfile,
        onAcceptClick = pinBuddyViewModel::acceptPinBuddy,
        onRejectClick = pinBuddyViewModel::rejectPinBuddy,
        onClickSearch = onClickSearch,
        onRefresh = pinBuddyViewModel::refreshView,
        isRefreshing = uiState.value.isRefreshing,
        getMorePinBuddy = pinBuddyViewModel::getMorePinBuddies,
        getMoreSentPinBuddy = pinBuddyViewModel::getMoreSentPinBuddies,
        getMoreReceivePinBuddy = pinBuddyViewModel::getMoreReceivePinBuddies
    )
}