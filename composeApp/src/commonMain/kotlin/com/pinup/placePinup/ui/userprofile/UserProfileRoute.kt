package com.pinup.placePinup.ui.userprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.ui.login.sns.rememberKakaoShareStrings
import com.pinup.placePinup.domain.model.ReportType
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
    onMoveReport: (Int, ReportType) -> Unit = {_, _ -> },
    onMoveMap: () -> Unit = {},
    viewModel: UserProfileViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val kakaoStrings = rememberKakaoShareStrings(uiState.value.member.profile.nickname)
    val onClickShare = remember(kakaoStrings) {
        { viewModel.shareMyProfile(kakaoStrings.title, kakaoStrings.content, kakaoStrings.button) }
    }
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
        onClickPlace = { kakaoPlaceId, reviewId ->
            viewModel.moveToPlaceOnMap(kakaoPlaceId, reviewId)
            onMoveMap()
        },
        onMovePints = onMovePints,
        onClickShare = onClickShare,
        onMoveDetail = onMovePintsDetail,
        getMorePints = viewModel::getMorePints,
        onClickBack = onClickBack,
        onClickBlockUser = viewModel::blockUser,
        onMoveReport = onMoveReport,
        onClickUnBlock = viewModel::undoBlockUser
    )
}
