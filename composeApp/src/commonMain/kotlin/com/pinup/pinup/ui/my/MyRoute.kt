package com.pinup.pinup.ui.my

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyRoute(
    myViewModel: MyViewModel = koinViewModel(),
    onAddPinBuddyClick: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveSetting: () -> Unit,
) {
    val uiState = myViewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
       // myViewModel.initMyInfo()
        onPauseOrDispose { }
    }

    MyScreen(
        member = uiState.value.member,
        photoReviews = uiState.value.photoReviews,
        textReviews = uiState.value.textReviews,
        onAddPinBuddyClick = onAddPinBuddyClick,
        onMovePinBuddy = onMovePinBuddy,
        onSettingClick = onMoveSetting
    )
}