package com.pinup.placePinup.ui.my.scrap

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.usecase.GetBookmarksUseCase
import com.pinup.placePinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.model.ChipState
import kotlinx.coroutines.launch


class ScrapViewModel (
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
) : BaseViewModel<ScrapUiState, UiEvent>(ScrapUiState()) {

    init {
        initMyInfo()
    }

    private fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        getScrapList()
        updateState {
            copy(
                profileUrl = memberInfo.profile.profilePictureUrl ?: "",
            )
        }
    }

    private fun getScrapList(
        chipState: ChipState = uiState.value.chipStates.first { it.isSelected },
        sortType: SortType = uiState.value.sortType
    ) = viewModelScope.launch {
        resultResponse(
            response = getBookmarksUseCase(
                sort = sortType,
                category = chipState.type,
                currentLatitude = "",
                currentLongitude = "",
            ),
            successCallback = {
                updateState {
                    copy(
                        scrapList = it,
                        chipStates = chipStates.map { chip ->
                            chip.copy(
                                isSelected = chip == chipState
                            )
                        },
                        sortType = sortType
                    )
                }
            }
        )
    }

    fun updateChipState(chipState: ChipState) {
        getScrapList(chipState = chipState)
    }

    fun updateSortType(sortType: SortType) {
        getScrapList(sortType = sortType)
    }
}

data class ScrapUiState(
    val scrapList: List<BookmarkedPlace> = emptyList(),
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
) : UiState