package com.pinup.placePinup.ui.image

import androidx.lifecycle.SavedStateHandle
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.util.Const

class DetailImageViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailImageUiState, DetailImageUiEvent>(DetailImageUiState()) {

    init {
        updateState {
            copy(
                position = savedStateHandle.get<Int>(Const.NavKey.DETAIL_IMAGE_POSITION),
                images = savedStateHandle.get<Array<String>>(Const.NavKey.DETAIL_IMAGE_ALL_IMAGES)?.toList() ?: emptyList()
            )
        }
    }
}

data class DetailImageUiState(
    val position: Int? = null,
    val images: List<String> = emptyList()
) : UiState

sealed class DetailImageUiEvent : UiEvent