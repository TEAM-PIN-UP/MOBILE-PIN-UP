package com.pinup.pinup.ui.my.pinch

import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.model.ChipState

class PinchWriteViewModel (
) : BaseViewModel<PinchWriteUiState, UiEvent>(PinchWriteUiState()) {

    fun updateTitle(title: String) {
        updateState {
            copy(
                title = title
            )
        }
    }

    fun updateDescription(des: String) {
        updateState {
            copy(
                description = des
            )
        }
    }
}

data class PinchWriteUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = "25.09.09",
    val scrapList: List<BookmarkedPlace> = emptyList(),
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
) : UiState