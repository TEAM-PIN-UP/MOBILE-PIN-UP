package com.pinup.pinup.ui.feed

import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState

class FeedViewModel(
) : BaseViewModel<FeedUiState, UiEvent>(FeedUiState()) {

}

data class FeedUiState(
    val pagingReview: PagingReview = PagingReview(),
): UiState