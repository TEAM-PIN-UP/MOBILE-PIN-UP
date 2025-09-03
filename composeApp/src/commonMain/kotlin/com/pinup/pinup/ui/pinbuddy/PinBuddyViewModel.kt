package com.pinup.pinup.ui.pinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.PinBuddyRequest
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.AcceptPinBuddyUseCase
import com.pinup.pinup.domain.usecase.DeletePinBuddyUseCase
import com.pinup.pinup.domain.usecase.DeleteRequestPinBuddyUseCase
import com.pinup.pinup.domain.usecase.GetPinBuddiesUseCase
import com.pinup.pinup.domain.usecase.GetReceivePinBuddyRequestsUseCase
import com.pinup.pinup.domain.usecase.GetSentPinBuddyRequestsUseCase
import com.pinup.pinup.domain.usecase.RejectPinBuddyUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class PinBuddyViewModel (
    private val getPinBuddiesUseCase: GetPinBuddiesUseCase,
    private val getSentPinBuddyRequestsUseCase: GetSentPinBuddyRequestsUseCase,
    private val getReceivePinBuddyRequestsUseCase: GetReceivePinBuddyRequestsUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val acceptPinBuddyUseCase: AcceptPinBuddyUseCase,
    private val rejectPinBuddyUseCase: RejectPinBuddyUseCase,
) : BaseViewModel<PinBuddyUiState, PinBuddyUiEvent>(PinBuddyUiState()) {
    private val pinBuddyPagination = Pagination()
    private val sentPinBuddyPagination = Pagination()
    private val receivePinBuddyPagination = Pagination()

    init {
       // initPinBuddies()
    }

    private fun initPinBuddies() = viewModelScope.launch {
        val pinBuddies = getPinBuddiesUseCase(pinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val sentPinBuddyRequests = getSentPinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val receivePinBuddyRequests = getReceivePinBuddyRequestsUseCase(receivePinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch

        updateState {
            copy(
                pinBuddies = pinBuddies.profiles,
                sentPinBuddyRequests = sentPinBuddyRequests.pinBuddyRequests,
                receivePinBuddyRequests = receivePinBuddyRequests.pinBuddyRequests,
            )
        }
    }

    fun deletePinBuddy(friendId: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinBuddyUseCase(friendId.toString()),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessDelete)
                updatePinBuddies()
            }
        )
    }

    fun deletePinBuddyRequest(memberId: Int) = viewModelScope.launch {
        resultResponse(
            response = deleteRequestPinBuddyUseCase(memberId),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessCancel)
                updatePinBuddyRequests()
            }
        )
    }

    fun acceptPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        resultResponse(
            response = acceptPinBuddyUseCase(friendRequestId),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessAccept)
                updatePinBuddies()
                updateReceivePinBuddyRequests()
            }
        )
    }

    fun rejectPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        resultResponse(
            response = rejectPinBuddyUseCase(friendRequestId),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessRefuse)
                updateReceivePinBuddyRequests()
            }
        )
    }

    private fun updateReceivePinBuddyRequests() = viewModelScope.launch {
        resultResponse(
            response = getReceivePinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        receivePinBuddyRequests = it.pinBuddyRequests
                    )
                }
            }
        )
    }

    private fun updatePinBuddies() = viewModelScope.launch {
        resultResponse(
            response = getPinBuddiesUseCase(pinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        pinBuddies = it.profiles,
                    )
                }
            }
        )
    }

    private fun updatePinBuddyRequests() = viewModelScope.launch {
        resultResponse(
            response = getSentPinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        sentPinBuddyRequests = it.pinBuddyRequests,
                    )
                }
            }
        )
    }
}

data class PinBuddyUiState(
    val pinBuddies: List<Profile> = emptyList(),
    val sentPinBuddyRequests: List<PinBuddyRequest> = emptyList(),
    val receivePinBuddyRequests: List<PinBuddyRequest> = emptyList(),
) : UiState

sealed interface PinBuddyUiEvent : UiEvent {
    data object SuccessDelete : PinBuddyUiEvent
    data object SuccessAccept : PinBuddyUiEvent
    data object SuccessRefuse : PinBuddyUiEvent
    data object SuccessCancel : PinBuddyUiEvent
}