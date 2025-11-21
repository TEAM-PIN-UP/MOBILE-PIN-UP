package com.pinup.placePinup.ui.userprofile

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.platform.ContextFactory
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserProfileRoute(
    contextFactory: ContextFactory,
    onClickDetail: (Int) -> Unit = {},
    onMovePints: (Int) -> Unit = {},
    onMovePintsDetail: (Int) -> Unit = {},
    onClickBack: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    viewModel: UserProfileViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    UserProfileScreen(
        member = uiState.value.member,
        photoReviews = uiState.value.pagingReview.reviews,
        pinchPageAble = uiState.value.pinchPageAble,
        onRequestCancel = viewModel::deleteRequestPinBuddy,
        onRemovePinBuddy = viewModel::deletePinBuddy,
        onRequestPinBuddy = viewModel::requestPinBuddy,
        onReceivedAccept = viewModel::acceptPinBuddy,
        onReceivedReject = viewModel::rejectPinBuddy,
        onClickLike = viewModel::likeChanged,
        onClickDetail = onClickDetail,
        onSettingClick = onSettingClick,
        onMovePints = onMovePints,
        onClickShare = viewModel::shareMyProfile,
        onMoveDetail = onMovePintsDetail,
        getMorePints = viewModel::getMorePints,
        onClickBack = onClickBack
    )
}
