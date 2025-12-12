package com.pinup.placePinup.ui.report

import androidx.lifecycle.SavedStateHandle
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState


class ReportViewModel (
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<UiState, ReportUiEvent>(UiState.Default) {
    val targetId = savedStateHandle.get<Int>(TARGET_ID) ?: -1

    companion object {
        private const val TARGET_ID = "targetId"

    }
}

sealed interface ReportUiEvent : UiEvent {
    data object SuccessReport : ReportUiEvent
}