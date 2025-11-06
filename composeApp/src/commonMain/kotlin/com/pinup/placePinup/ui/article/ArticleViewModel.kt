package com.pinup.placePinup.ui.article

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.domain.usecase.GetEditorPintsUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ArticleViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getEditorPintsUseCase: GetEditorPintsUseCase
) : BaseViewModel<ArticleUiState, UiEvent>(ArticleUiState()) {

    init {
        getMyProfile()
    }

    fun getArticleList() = viewModelScope.launch {
        val request = GetEditorPintsRequest(
            swLatitude = "0.0",
            swLongitude = "0.0",
            neLatitude = "0.0",
            neLongitude = "0.0",
        )
        resultResponse(
            response = getEditorPintsUseCase(request),
            successCallback = {
                updateState {
                    copy(
                        pintsListItem = it,
                        isRefreshing = false
                    )
                }
            }
        )
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

    fun refreshView() = viewModelScope.launch {
        updateState {
            copy(
                isRefreshing = true
            )
        }
        delay(1.seconds)
        getArticleList()
    }

}

data class ArticleUiState(
    val pintsListItem: List<PinchListItem> = emptyList(),
    val prevCursor: Int? = null,
    val profileUrl: String = "",
    val isRefreshing: Boolean = false,
): UiState