package com.pinup.pinup.ui.article

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.DeleteRecentSearchUseCase
import com.pinup.pinup.domain.usecase.GetEditorPintsUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.GetRecentSearchUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.SaveRecentSearchUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getEditorPintsUseCase: GetEditorPintsUseCase
) : BaseViewModel<ArticleUiState, UiEvent>(ArticleUiState()) {

    init {
        getMyProfile()
    }

    fun getArticleList() = viewModelScope.launch {
        val request = GetReviewedPlacesRequest(
            swLatitude = "",
            swLongitude = "",
            neLatitude = "",
            neLongitude = "",
        )
        resultResponse(
            response = getEditorPintsUseCase(request),
            successCallback = {
                updateState {
                    copy(
                        pintsListItem = it
                    )
                }
            }
        )
    }

    private fun getMyProfile() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest {
                updateState {
                    copy(
                        profileUrl = it.profileUrl
                    )
                }
            }
    }

}

data class ArticleUiState(
    val pintsListItem: List<PinchListItem> = emptyList(),
    val prevCursor: Int? = null,
    val profileUrl: String = "",
): UiState