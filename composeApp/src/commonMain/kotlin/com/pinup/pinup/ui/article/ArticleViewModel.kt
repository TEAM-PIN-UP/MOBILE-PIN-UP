package com.pinup.pinup.ui.article

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.DeleteRecentSearchUseCase
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
) : BaseViewModel<ArticleUiState, UiEvent>(ArticleUiState()) {

    init {
        getMyProfile()
    }

    fun getArticleList() = viewModelScope.launch {
        //TODO 구현해야 함.
//        if (!uiState.value.pagingArticle.hasNext) return@launch
//        resultResponse(
//            response = getFeedUseCase(uiState.value.pagingReview.nextCursor, memberId = null, keyword = uiState.value.searchText.ifEmpty { null }),
//            successCallback = {
//                updateState {
//                    copy(
//                        prevCursor = uiState.value.pagingReview.nextCursor,
//                        pagingReview = it.copy(
//                            reviews = pagingReview.reviews + it.reviews
//                        ),
//                    )
//                }
//            }
//        )
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
    val pagingArticle: PagingArticle = PagingArticle(),
    val prevCursor: Int? = null,
    val profileUrl: String = "",
): UiState