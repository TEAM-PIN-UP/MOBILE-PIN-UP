package com.pinup.pinup.ui.my.pinch

import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Place
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

    //TODO 검색한 장소 클릭하면 name 업데이트와 동시에 앞에 index 숫자 붙이기
    fun updatePinchName(index: Int, name: String) {
        updateState {
            copy(
                pinchList = pinchList.mapIndexed { i, place ->
                    if (i == index) {
                        place.copy(name = name)
                    } else {
                        place
                    }
                }
            )
        }
    }

    fun moveItem(from: Int, to: Int) {
        if (from == to) return
        updateState {
            val new = pinchList.toMutableList().apply {
                add(to, removeAt(from))
            }
            copy(pinchList = new)
        }
    }
}

data class PinchWriteUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = "25.09.09",
    val pinchList: List<Place> = List(5) { Place() },
    val searchedList: List<Place> = emptyList(),
    val scrapList: List<BookmarkedPlace> = emptyList(),
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
) : UiState