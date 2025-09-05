package com.pinup.pinup.ui.my

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Texts
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyRoute(
    myViewModel: MyViewModel = koinViewModel(),
    onAddPinBuddyClick: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveSetting: () -> Unit,
    onClickBottomNav: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
) {
    val uiState = myViewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        myViewModel.initMyInfo()
        onPauseOrDispose { }
    }

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = Texts.PinLog.DELETE_DIALOG_TITLE,
            descriptionText = Texts.PinLog.DELETE_DIALOG_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_DELETE,
            onLeftButtonClick = {
                isShowDeleteDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteDialog.value = false
                myViewModel::deleteReview
            },
        )
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
        onClickDelete = {
            isShowDeleteDialog.value = true
        },
        onClickPinLog = onClickPinLog,
        onClickLike = myViewModel::likeChanged,
        onClickDetail = onClickDetail
    )
}