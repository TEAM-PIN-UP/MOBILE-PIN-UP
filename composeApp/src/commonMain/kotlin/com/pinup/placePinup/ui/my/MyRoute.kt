package com.pinup.placePinup.ui.my

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Texts
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MyRoute(
    contextFactory: ContextFactory,
    myViewModel: MyViewModel = koinViewModel(parameters = { parametersOf(contextFactory) }),
    onProfileModifyClick: () -> Unit,
    onMovePinBuddy: () -> Unit,
    onMoveSetting: () -> Unit,
    onClickBottomNav: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onClickMoreScrap: () -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    onMovePintsDetail: (Int) -> Unit = {},
) {
    val uiState = myViewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isClickedFeedId = remember { mutableStateOf(-1) }

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
                myViewModel.deleteReview(isClickedFeedId.value)
                isShowDeleteDialog.value = false
            },
        )
    }

    MyScreen(
        member = uiState.value.member,
        reviews = uiState.value.pagingReview.reviews,
        scrapList = uiState.value.scrapList,
        pinchPageAble = uiState.value.pinchPageAble,
        isRefreshing = uiState.value.isRefreshing,
        onProfileModifyClick = onProfileModifyClick,
        onMovePinBuddy = onMovePinBuddy,
        onSettingClick = onMoveSetting,
        onClickBottomNav = onClickBottomNav,
        onClickEdit = {
            onClickEdit(it)
        },
        onClickDelete = {
            isClickedFeedId.value = it
            isShowDeleteDialog.value = true
        },
        onClickPinLog = onClickPinLog,
        onClickLike = myViewModel::likeChanged,
        onClickDetail = onClickDetail,
        onClickShare = myViewModel::shareMyProfile,
        onClickMoreScrap = onClickMoreScrap,
        onMovePlaceDetail = onMovePlaceDetail,
        onMovePinchWrite = onMovePinchWrite,
        getMorePints = myViewModel::getMorePints,
        onMoveDetail = onMovePintsDetail,
        onRefresh = myViewModel::refreshView,
    )
}