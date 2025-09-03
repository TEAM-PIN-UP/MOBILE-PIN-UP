package com.pinup.pinup.ui.my

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyRoute(
    myViewModel: MyViewModel = koinViewModel(),
    onAddPinBuddyClick: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveSetting: () -> Unit,
    onClickBottomNav: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
) {
    val uiState = myViewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
       // myViewModel.initMyInfo()
        onPauseOrDispose { }
    }

    MyScreen(
        member = uiState.value.member,
        reviews = uiState.value.photoReviews,
        onAddPinBuddyClick = onAddPinBuddyClick,
        onMovePinBuddy = onMovePinBuddy,
        onSettingClick = onMoveSetting,
        onClickBottomNav = onClickBottomNav,
        onClickEdit = {
            onClickEdit(it)
        },
        onClickDelete = myViewModel::deleteReview,
    )
}