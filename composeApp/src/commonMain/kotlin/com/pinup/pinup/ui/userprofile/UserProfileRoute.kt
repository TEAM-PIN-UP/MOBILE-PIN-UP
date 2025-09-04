package com.pinup.pinup.ui.userprofile

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserProfileRoute(
    onBackPressed: () -> Unit,
    onClickDetail: (Int) -> Unit = {},
    viewModel: UserProfileViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    UserProfileScreen(
        member = uiState.value.member,
        photoReviews = uiState.value.photoReviews,
        onRequestCancel = viewModel::deleteRequestPinBuddy,
        onRemovePinBuddy = viewModel::deletePinBuddy,
        onRequestPinBuddy = viewModel::requestPinBuddy,
        onClickLike = viewModel::likeChanged,
        onClickDetail = onClickDetail
    )
}
