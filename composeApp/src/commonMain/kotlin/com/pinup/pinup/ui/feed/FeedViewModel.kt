package com.pinup.pinup.ui.feed

import androidx.lifecycle.viewModelScope
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

class FeedViewModel(
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase
) : BaseViewModel<FeedUiState, UiEvent>(FeedUiState()) {

    init {
        getMyProfile()
        getRecentSearchList()
    }

    fun getFeedList(id: Int = uiState.value.pagingReview.nextCursor) = viewModelScope.launch {
        resultResponse(
            response = getFeedUseCase(id, memberId = null, keyword = uiState.value.searchText.ifEmpty { null }),
            successCallback = {
                updateState {
                    copy(
                        prevCursor = id,
                        pagingReview = it.copy(
                            reviews = pagingReview.reviews + it.reviews
                        ),
                    )
                }
            }
        )
    }

    fun deleteReview(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(id),
            successCallback = {
                getFeedList(0)
            }
        )
    }

    fun likeChanged(id: Int, isLike: Boolean) = viewModelScope.launch {
        resultResponse(
            response = postReviewLikeChangeUseCase(id, isLike),
            successCallback = {
                handleSuccessLikeChanged(id)
            }
        )
    }

    private fun handleSuccessLikeChanged(id: Int) = viewModelScope.launch {
        resultResponse(
            response = getFeedUseCase(id, 1, null, null),
            successCallback = { result ->
                updateState {
                    copy(
                        pagingReview = pagingReview.copy(
                            reviews = pagingReview.reviews.map {
                                if (it.id == result.reviews[0].id) {
                                    result.reviews[0]
                                } else {
                                    it
                                }
                            }
                        )
                    )
                }
            }
        )
    }

    fun updateSearchMode() {
        updateState {
            copy(
                searchMode = !searchMode
            )
        }
    }

    fun updateSearchText(text: String) {
        updateState {
            copy(
                searchText = text
            )
        }
    }

    fun onClickSearch() {
        getFeedList(id = 0)
        saveRecentSearch()
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

    private fun getRecentSearchList() = viewModelScope.launch {
        getRecentSearchUseCase()
            .collectLatest {
                updateState {
                    copy(
                        recentSearchList = it
                    )
                }
            }
    }

    fun deleteRecentSearch(index: Int) = viewModelScope.launch {
        deleteRecentSearchUseCase(index)
    }

    private fun saveRecentSearch() = viewModelScope.launch {
        saveRecentSearchUseCase(uiState.value.searchText)
    }
}

data class FeedUiState(
    val pagingReview: PagingReview = PagingReview(),
    val prevCursor: Int = 0,
    val searchMode: Boolean = false,
    val searchText: String = "",
    val profileUrl: String = "",
    val recentSearchList: List<String> = emptyList()
): UiState