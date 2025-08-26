package com.pinup.pinup.ui.pinlogDetail

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.theme.Texts
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import rememberSimpleToastState

@Composable
fun PinlogDetailRoute(
    viewModel: PinlogDetailViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
) {
    val toast = rememberSimpleToastState()
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
        placeName = uiState.pinlogDetail.placeName,
        visitDate = uiState.pinlogDetail.visitedDate,
        profileImage = uiState.pinlogDetail.writerProfileImageUrl,
        userName = uiState.pinlogDetail.writerName,
        reviewCount = uiState.pinlogDetail.authorReviewCount,
        starRating = uiState.pinlogDetail.starRating,
        createdDate = uiState.pinlogDetail.createdAt,
        reviewImageUrls = uiState.pinlogDetail.reviewImageUrls,
        content = uiState.pinlogDetail.content,
        isOwn = uiState.pinlogDetail.isOwn,
        likeCount = uiState.pinlogDetail.likeCount,
        isLikedByUse = uiState.pinlogDetail.isLikedByUser,
        commentCount = uiState.pinlogDetail.commentCount,
        comments = uiState.pinlogDetail.comments,
        isScrapByUser = uiState.pinlogDetail.isScrapByUser,
        onBackPressed = onBackPressed,
        query = uiState.myComment,
        replyId = uiState.clickedReplyId,
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
        updateNonFocusMode = {
            if(uiState.isEditComment || uiState.clickedReplyId != null) viewModel::updateNormalMode
        },
        updateReplyCommentId = viewModel::updateClickedReplyId
    )
}
