package com.pinup.placePinup.ui.my
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
    onMovePints: (Int) -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    onMovePintsDetail: (Int) -> Unit = {},
    onMoveNotification: () -> Unit = {},
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
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
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
        pagingReview = uiState.value.pagingReview,
        scrapList = uiState.value.scrapList,
        pinchPageAble = uiState.value.pinchPageAble,
        isRefreshing = uiState.value.isRefreshing,
        onAlarmClick = onMoveNotification,
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
        onMovePints = onMovePints,
        onMovePinchWrite = onMovePinchWrite,
        getMorePints = myViewModel::getMorePints,
        onMoveDetail = onMovePintsDetail,
        onRefresh = myViewModel::refreshView,
        getMoreReviews = {
            myViewModel.getFeedList(id = uiState.value.pagingReview.nextCursor, memberId = uiState.value.member.profile.memberId)
        }
    )
}