package com.pinup.pinup.ui.my.scrap

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.kakaoShare
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.model.ChipState
import kotlinx.coroutines.launch
import kotlin.collections.plus


class ScrapViewModel (
    private val getMemberInfoUseCase: GetMemberInfoUseCase
) : BaseViewModel<ScrapUiState, UiEvent>(ScrapUiState()) {

    init {
        initMyInfo()
    }

    private fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        updateState {
            copy(
                profileUrl = memberInfo.profile.profilePictureUrl ?: "",
            )
        }
    }

}

data class ScrapUiState(
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
) : UiState