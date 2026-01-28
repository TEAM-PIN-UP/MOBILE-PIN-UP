package com.pinup.placePinup.ui.pinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.Pagination
import com.pinup.placePinup.domain.model.PagingPinBuddy
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.usecase.AcceptPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.DeletePinBuddyUseCase
import com.pinup.placePinup.domain.usecase.DeleteRequestPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.placePinup.domain.usecase.GetPinBuddiesUseCase
import com.pinup.placePinup.domain.usecase.GetReceivePinBuddyRequestsUseCase
import com.pinup.placePinup.domain.usecase.GetSentPinBuddyRequestsUseCase
import com.pinup.placePinup.domain.usecase.RejectPinBuddyUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PinBuddyViewModel (
    private val getPinBuddiesUseCase: GetPinBuddiesUseCase,
    private val getSentPinBuddyRequestsUseCase: GetSentPinBuddyRequestsUseCase,
    private val getReceivePinBuddyRequestsUseCase: GetReceivePinBuddyRequestsUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val acceptPinBuddyUseCase: AcceptPinBuddyUseCase,
    private val rejectPinBuddyUseCase: RejectPinBuddyUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase
) : BaseViewModel<PinBuddyUiState, PinBuddyUiEvent>(PinBuddyUiState()) {

    private var pinBuddyCurrentPage = 0
    private var sentPinBuddyCurrentPage = 0
    private var receivePinBuddyCurrentPage = 0

    fun initPinBuddies() = viewModelScope.launch {
        initMyInfo()
        initMyPinBuddies()
        initSentPinBuddyRequests()
        initReceivePinBuddies()

        updateState {
            copy(
                isRefreshing = false
            )
        }
    }

    private fun initMyPinBuddies() {
        pinBuddyCurrentPage = 0
        getMyPinBuddies()
    }

    private fun getMyPinBuddies() = viewModelScope.launch {
        resultResponse(
            response = getPinBuddiesUseCase(pinBuddyCurrentPage, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        pinBuddies = if (pinBuddyCurrentPage == 0) it else pinBuddies.copy(
                            profiles = pinBuddies.profiles + it.profiles
                        )
                    )
                }
            }
        )
    }

    fun getMorePinBuddies() {
        if (pinBuddyCurrentPage + 1 == uiState.value.pinBuddies.totalPages) return
        pinBuddyCurrentPage++
        getMyPinBuddies()
    }

    private fun initSentPinBuddyRequests() {
        sentPinBuddyCurrentPage = 0
        getSentPinBuddyRequests()
    }

    private fun getSentPinBuddyRequests() = viewModelScope.launch {
        resultResponse(
            response = getSentPinBuddyRequestsUseCase(sentPinBuddyCurrentPage, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        sentPinBuddyRequests = if (sentPinBuddyCurrentPage == 0) it else sentPinBuddyRequests.copy(
                            pinBuddyRequests = sentPinBuddyRequests.pinBuddyRequests + it.pinBuddyRequests
                        )
                    )
                }
            }
        )
    }

    fun getMoreSentPinBuddies() {
        if (sentPinBuddyCurrentPage + 1 == uiState.value.sentPinBuddyRequests.totalPages) return
        sentPinBuddyCurrentPage++
        getSentPinBuddyRequests()
    }

    private fun initReceivePinBuddies() {
        receivePinBuddyCurrentPage = 0
        getReceivePinBuddies()
    }

    private fun getReceivePinBuddies() = viewModelScope.launch {
        resultResponse(
            response = getReceivePinBuddyRequestsUseCase(receivePinBuddyCurrentPage, Pagination.DEFAULT_PAGE_SIZE),
            successCallback = {
                updateState {
                    copy(
                        receivePinBuddyRequests = if (receivePinBuddyCurrentPage == 0) it else receivePinBuddyRequests.copy(
                            pinBuddyRequests = receivePinBuddyRequests.pinBuddyRequests + it.pinBuddyRequests
                        )
                    )
                }
            }
        )
    }

    fun getMoreReceivePinBuddies() {
        if (receivePinBuddyCurrentPage + 1== uiState.value.receivePinBuddyRequests.totalPages) return
        receivePinBuddyCurrentPage++
        getReceivePinBuddies()
    }

    private fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        updateState {
            copy(
                profileUrl = memberInfo.profile.profilePictureUrl ?: "",
            )
        }
    }

    fun deletePinBuddy(friendId: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinBuddyUseCase(friendId.toString()),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessDelete)
                updatePinBuddies(friendId)
            }
        )
    }

    private fun updatePinBuddies(buddyId: Int) {
        updateState {
            copy(
                pinBuddies = pinBuddies.copy(
                    profiles = pinBuddies.profiles.filterNot { it.memberId == buddyId }
                )
            )
        }
    }

    fun deletePinBuddyRequest(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deleteRequestPinBuddyUseCase(id),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessCancel)
                updatePinBuddyRequests(id)
            }
        )
    }

    private fun updatePinBuddyRequests(id: Int) {
        updateState {
            copy(
                sentPinBuddyRequests = sentPinBuddyRequests.copy(
                    pinBuddyRequests = sentPinBuddyRequests.pinBuddyRequests.filterNot { it.id == id }
                )
            )
        }
    }

    fun acceptPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        resultResponse(
            response = acceptPinBuddyUseCase(friendRequestId),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessAccept)
                initMyPinBuddies()
                updateReceivePinBuddyRequests(friendRequestId)
            }
        )
    }

    fun rejectPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        resultResponse(
            response = rejectPinBuddyUseCase(friendRequestId),
            successCallback = {
                emitEvent(PinBuddyUiEvent.SuccessRefuse)
                updateReceivePinBuddyRequests(friendRequestId)
            }
        )
    }

    private fun updateReceivePinBuddyRequests(id: Int) {
        updateState {
            copy(
                receivePinBuddyRequests = receivePinBuddyRequests.copy(
                    pinBuddyRequests = receivePinBuddyRequests.pinBuddyRequests.filterNot { it.id == id }
                )
            )
        }
    }

    fun refreshView() = viewModelScope.launch {
        updateState {
            copy(
                isRefreshing = true
            )
        }
        delay(1.seconds)
        initPinBuddies()
    }
}

data class PinBuddyUiState(
    val pinBuddies: PagingPinBuddy = PagingPinBuddy(),
    val isRefreshing: Boolean = false,
    val sentPinBuddyRequests: PagingPinBuddyRequest = PagingPinBuddyRequest(),
    val receivePinBuddyRequests: PagingPinBuddyRequest = PagingPinBuddyRequest(),
    val profileUrl: String = ""
) : UiState

sealed interface PinBuddyUiEvent : UiEvent {
    data object SuccessDelete : PinBuddyUiEvent
    data object SuccessAccept : PinBuddyUiEvent
    data object SuccessRefuse : PinBuddyUiEvent
    data object SuccessCancel : PinBuddyUiEvent
}