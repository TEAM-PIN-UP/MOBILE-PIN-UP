package com.pinup.pinup.ui.userprofile

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserProfileRoute(
    contextFactory: ContextFactory,
    onClickDetail: (Int) -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    onMovePintsDetail: (Int) -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: UserProfileViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    UserProfileScreen(
        member = uiState.value.member,
        photoReviews = uiState.value.pagingReview.reviews,
        pinchPageAble = uiState.value.pinchPageAble,
        profileUrl = uiState.value.profileUrl,
        onRequestCancel = viewModel::deleteRequestPinBuddy,
        onRemovePinBuddy = viewModel::deletePinBuddy,
        onRequestPinBuddy = viewModel::requestPinBuddy,
        onClickLike = viewModel::likeChanged,
        onClickDetail = onClickDetail,
        onMovePinchWrite = onMovePinchWrite,
        onClickShare = viewModel::shareMyProfile,
        onMoveDetail = onMovePintsDetail,
        getMorePints = viewModel::getMorePints,
        onClickBottomNav = onClickBottomNav
    )
}
