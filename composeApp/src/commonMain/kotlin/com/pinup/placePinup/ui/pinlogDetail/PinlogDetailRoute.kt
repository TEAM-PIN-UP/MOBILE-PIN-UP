package com.pinup.placePinup.ui.pinlogDetail
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.domain.model.ReportType
import com.pinup.placePinup.ui.component.PDialog
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
    onMoveDetailImage: (Int, List<String>) -> Unit = {_,_ -> },
    onMoveReport: (Int, ReportType) -> Unit = {_, _ -> },
) {
    val toast = rememberToastState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isShowDeleteCommentDialog = remember { mutableStateOf(false) }
    val isShowBlockUserDialog = remember { mutableStateOf(false) }
    var clickedCommentId by remember { mutableStateOf(0) }

    var clickedTargetId by remember { mutableStateOf(0) }
    var clickedTargetName by remember { mutableStateOf("") }
    val deletePinlogText = stringResource(Res.string.toast_delete_pinlog)
    val blockSuccessTemplate = stringResource(Res.string.toast_block_success)
    val alreadyBlockText = stringResource(Res.string.toast_already_block_user)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinlogUiEvent.SuccessDelete -> {
                    toast.show(deletePinlogText)
                    onBackPressed()
                }

                is PinlogUiEvent.OnMoveUserProfile -> {
                    onMoveUserProfile(it.name)
                }

                PinlogUiEvent.SuccessBlockUser -> {
                    toast.show(blockSuccessTemplate.replace("%s", clickedTargetName))
                }

                PinlogUiEvent.ErrorBlockUser -> {
                    toast.show(alreadyBlockText)
                }
            }
        }
    }

    PToastHost(state = toast)

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.pin_log_delete_dialog_title),
            descriptionText = stringResource(Res.string.pin_log_delete_dialog_description),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
            onLeftButtonClick = {
                isShowDeleteDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteDialog.value = false
                viewModel.deleteReview()
            },
        )
    }

    if (isShowBlockUserDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.report_block_dialog_title, clickedTargetName),
            descriptionText = stringResource(Res.string.report_block_dialog_content),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_block),
            onLeftButtonClick = {
                isShowBlockUserDialog.value = false
            },
            onRightButtonClick = {
                isShowBlockUserDialog.value = false
                viewModel.blockUser(clickedTargetId)
            },
        )
    }

    if (isShowDeleteCommentDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.pin_log_delete_comment_dialog_title),
            descriptionText = stringResource(Res.string.pin_log_delete_comment_dialog_description),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
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
        onMoveDetailImage = onMoveDetailImage,
        onClickLike = viewModel::likeChanged,
        userInfo = uiState.userInfo,
        onRefresh = viewModel::refreshView,
        onMoveReport = onMoveReport,
        onClickBlock = { userId, userName ->
            clickedTargetId = userId
            clickedTargetName = userName
            isShowBlockUserDialog.value = true
        }
    )
}
