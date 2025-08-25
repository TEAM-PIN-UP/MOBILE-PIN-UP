package com.pinup.pinup.ui.feed

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class FeedViewModel(
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getFeedUseCase: GetFeedUseCase
) : BaseViewModel<FeedUiState, UiEvent>(FeedUiState()) {

    init {
        getFeedList()
    }

    fun getFeedList(id: Int = uiState.value.pagingReview.nextCursor) = viewModelScope.launch {
        resultResponse(
            response = getFeedUseCase(id),
            successCallback = {
                updateState {
                    copy(
                        pagingReview = it
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

}

data class FeedUiState(
    val pagingReview: PagingReview = PagingReview(),
): UiState