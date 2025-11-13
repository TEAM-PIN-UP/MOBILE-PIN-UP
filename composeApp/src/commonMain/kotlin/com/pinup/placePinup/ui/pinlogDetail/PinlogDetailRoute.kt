package com.pinup.placePinup.ui.pinlogDetail

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.theme.Texts
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import rememberToastState

@Composable
fun PinlogDetailRoute(
    viewModel: PinlogDetailViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
) {
    val toast = rememberToastState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isShowDeleteCommentDialog = remember { mutableStateOf(false) }
    var clickedCommentId by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinlogUiEvent.SuccessDelete -> {
                    toast.show(Texts.Toast.DELETE_PINLOG)
                    onBackPressed()
                }

                is PinlogUiEvent.OnMoveUserProfile -> {
                    onMoveUserProfile(it.name)
                }
            }
        }
    }

    PToastHost(state = toast)

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
                viewModel.deleteReview()
            },
        )
    }

    if (isShowDeleteCommentDialog.value) {
        PDialog(
            titleText = Texts.PinLog.DELETE_COMMENT_DIALOG_TITLE,
            descriptionText = Texts.PinLog.DELETE_COMMENT_DIALOG_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_DELETE,
            onLeftButtonClick = {
                isShowDeleteCommentDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteCommentDialog.value = false
                viewModel.deleteComment(clickedCommentId)
            },
        )
    }

    PinlogDetailScreen(
        pinlogDetail = uiState.pinlogDetail,
        onBackPressed = onBackPressed,
        query = uiState.myComment,
        replyId = uiState.clickedReplyId,
        isRefreshing = uiState.isRefreshing,
        onValueChange = viewModel::updateMyComment,
        onClickEdit = {
            onClickEdit(viewModel.reviewId)
        },
        onClickDelete = {
            isShowDeleteDialog.value = true
        },
        onClickUploadComment = viewModel::uploadComment,
        onClickEditComment = viewModel::updateEditMode,
        onClickDeleteComment = {
            clickedCommentId = it
            isShowDeleteCommentDialog.value = true
        },
        onClickProfile = viewModel::onProfileClick,
        updateNonFocusMode = {
            if(uiState.isEditComment || uiState.clickedReplyId != null) viewModel::updateNormalMode
        },
        updateReplyCommentId = viewModel::updateClickedReplyId,
        onMovePlaceDetail = onMovePlaceDetail,
        onClickLike = viewModel::likeChanged,
        userInfo = uiState.userInfo,
        onRefresh = viewModel::refreshView
    )
}
