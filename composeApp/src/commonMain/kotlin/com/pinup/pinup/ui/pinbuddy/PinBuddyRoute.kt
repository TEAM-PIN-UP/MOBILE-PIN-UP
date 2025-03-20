package com.pinup.pinup.ui.pinbuddy

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PinBuddyRoute(
    onBackPressed: () -> Unit,
    onMoveUserProfile: (Int) -> Unit,
    pinBuddyViewModel: PinBuddyViewModel = koinViewModel()
) {
    val uiState = pinBuddyViewModel.uiState.collectAsStateWithLifecycle()
    PinBuddyScreen(
        pinBuddies = uiState.value.pinBuddies.toPersistentList(),
        sentPinBuddyRequests = uiState.value.sentPinBuddyRequests.toPersistentList(),
        receivePinBuddyRequests = uiState.value.receivePinBuddyRequests.toPersistentList(),
        onBackPressed = onBackPressed,
        onDeletePinBuddy = pinBuddyViewModel::deletePinBuddy,
        onDeletePinBuddyRequest = pinBuddyViewModel::deletePinBuddyRequest,
        onProfileClick = onMoveUserProfile,
        onAcceptClick = pinBuddyViewModel::acceptPinBuddy,
        onRejectClick = pinBuddyViewModel::rejectPinBuddy
    )
}