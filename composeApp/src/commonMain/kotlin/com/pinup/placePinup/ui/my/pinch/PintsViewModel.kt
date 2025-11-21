package com.pinup.placePinup.ui.my.pinch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.usecase.GetPintsUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.model.ChipState
import kotlinx.coroutines.launch
import kotlin.collections.plus


class PintsViewModel (
    savedStateHandle: SavedStateHandle,
    private val getPintsUseCase: GetPintsUseCase,
) : BaseViewModel<PintsUiState, UiEvent>(PintsUiState()) {
    val memberId = savedStateHandle.get<Int>(MEMBER_ID) ?: -1

    init {
        getPintsList()
    }

    private fun getPintsList() = viewModelScope.launch {
        val pageAble = PageAble()
        resultResponse(
            response = getPintsUseCase(memberId, pageAble),
            successCallback = {
                updateState {
                    copy(
                        pintsPageAble = it,
                        nowPage = nowPage + 1
                    )
                }
            }
        )
    }

    fun getMorePints() = viewModelScope.launch {
        val pageAble = PageAble(
            page = uiState.value.nowPage
        )
        resultResponse(
            response = getPintsUseCase(memberId, pageAble),
            successCallback = {
                updateState {
                    copy(
                        pintsPageAble = it.copy(
                            content = uiState.value.pintsPageAble.content + it.content
                        ),
                        nowPage = nowPage + 1
                    )
                }
            }
        )
    }

    companion object {
        private const val MEMBER_ID = "memberId"

    }
}

data class PintsUiState(
    val pintsPageAble: PintsPageAble = PintsPageAble(),
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
    val nowPage: Int = 1,
) : UiState