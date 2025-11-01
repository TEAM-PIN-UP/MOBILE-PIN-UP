package com.pinup.pinup.ui.feed

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.DeleteRecentSearchUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.pinup.domain.usecase.GetRecentSearchUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.SaveRecentSearchUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.map.MapUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FeedViewModel(
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
) : BaseViewModel<FeedUiState, FeedUiEvent>(FeedUiState()) {

    init {
        getMyProfile()
        getRecentSearchList()
    }

    fun getFeedList() = viewModelScope.launch {
        resultResponse(
            response = getFeedUseCase(null, memberId = null, keyword = uiState.value.searchText.ifEmpty { null }),
            successCallback = {
                updateState {
                    copy(
                        pagingReview = it,
                        isRefreshing = false
                    )
                }
            }
        )
    }

    fun getMoreFeed() = viewModelScope.launch {
        if (!uiState.value.pagingReview.hasNext) return@launch
        resultResponse(
            response = getFeedUseCase(uiState.value.pagingReview.nextCursor, memberId = null, keyword = uiState.value.searchText.ifEmpty { null }),
            successCallback = {
                updateState {
                    copy(
                        prevCursor = uiState.value.pagingReview.nextCursor,
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
                updateState {
                    copy(
                        pagingReview = uiState.value.pagingReview.copy (
                            reviews = uiState.value.pagingReview.reviews.filter { it.id != id }
                        ),
                    )
                }
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
            response = getPinlogDetailUseCase(id),
            successCallback = { result ->
                updateState {
                    copy(
                        pagingReview = pagingReview.copy(
                            reviews = pagingReview.reviews.map {
                                if (it.id == id) {
                                    it.copy(
                                        isLikedByUser = result.isLikedByUser,
                                        likeCount = result.likeCount
                                    )
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
        getFeedList()
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

    fun refreshView() = viewModelScope.launch {
        updateState {
            copy(
                isRefreshing = true
            )
        }
        delay(1.seconds)
        getFeedList()
    }

    fun onProfileClick(name: String) = viewModelScope.launch {
        getMyProfileUseCase().collectLatest {
            if(it.name == name) emitEvent(FeedUiEvent.OnMoveUserProfile(name))
        }
    }
}

data class FeedUiState(
    val pagingReview: PagingReview = PagingReview(),
    val isRefreshing: Boolean = false,
    val prevCursor: Int? = null,
    val searchMode: Boolean = false,
    val searchText: String = "",
    val profileUrl: String = "",
    val recentSearchList: List<String> = emptyList()
): UiState

sealed interface FeedUiEvent : UiEvent {
    data class OnMoveUserProfile(val name: String): FeedUiEvent
}