package com.pinup.pinup.ui.pinlogDetail

import PToastHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    PinlogDetailScreen(
        onBackPressed = onBackPressed,
        query = uiState.myComment,
        onValueChange = viewModel::updateMyComment,
        onClickEdit = {
            onClickEdit(viewModel.reviewId)
        },
        onClickDelete = viewModel::deleteReview,
        onClickUploadComment = viewModel::uploadComment,
        onClickEditComment = viewModel::updateEditMode,
        onClickDeleteComment = viewModel::deleteComment,
        updateNonFocusMode = {
            if(uiState.isEditComment || uiState.clickedReplyId != null) viewModel::updateNormalMode
        },
        updateReplyCommentId = viewModel::updateClickedReplyId
    )
}
