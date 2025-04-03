package com.pinup.pinup.ui.pinbuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
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
import com.pinup.pinup.platform.hLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PinBuddyViewModel (
    private val getPinBuddiesUseCase: GetPinBuddiesUseCase,
    private val getSentPinBuddyRequestsUseCase: GetSentPinBuddyRequestsUseCase,
    private val getReceivePinBuddyRequestsUseCase: GetReceivePinBuddyRequestsUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val acceptPinBuddyUseCase: AcceptPinBuddyUseCase,
    private val rejectPinBuddyUseCase: RejectPinBuddyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PinBuddyUiState())
    val uiState: StateFlow<PinBuddyUiState>
        get() = _uiState.asStateFlow()
    private val pinBuddyPagination = Pagination()
    private val sentPinBuddyPagination = Pagination()
    private val receivePinBuddyPagination = Pagination()

    init {
        initPinBuddies()
    }

    private fun initPinBuddies() = viewModelScope.launch {
        val pinBuddies = getPinBuddiesUseCase(pinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val sentPinBuddyRequests = getSentPinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val receivePinBuddyRequests = getReceivePinBuddyRequestsUseCase(receivePinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE).getSuccessOrNull() ?: return@launch


        _uiState.update {
            it.copy(
                pinBuddies = pinBuddies.profiles,
                sentPinBuddyRequests = sentPinBuddyRequests.pinBuddyRequests,
                receivePinBuddyRequests = receivePinBuddyRequests.pinBuddyRequests,
            )
        }
    }

    fun deletePinBuddy(friendId: Int) = viewModelScope.launch {
        when (val result = deletePinBuddyUseCase(friendId.toString())) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                updatePinBuddies()
            }
        }
    }

    fun deletePinBuddyRequest(memberId: Int) = viewModelScope.launch {
        when (val result = deleteRequestPinBuddyUseCase(memberId)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                updatePinBuddyRequests()
            }
        }
    }

    fun acceptPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        when (val result = acceptPinBuddyUseCase(friendRequestId)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                updatePinBuddies()
                updateReceivePinBuddyRequests()
            }
        }
    }

    fun rejectPinBuddy(friendRequestId: Int) = viewModelScope.launch {
        when (val result = rejectPinBuddyUseCase(friendRequestId)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                updateReceivePinBuddyRequests()
            }
        }
    }

    private fun updateReceivePinBuddyRequests() = viewModelScope.launch {
        when (val result = getReceivePinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        receivePinBuddyRequests = result.data.pinBuddyRequests,
                    )
                }
            }
        }
    }

    private fun updatePinBuddies() = viewModelScope.launch {
        when (val result = getPinBuddiesUseCase(pinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        pinBuddies = result.data.profiles,
                    )
                }
            }
        }
    }

    private fun updatePinBuddyRequests() = viewModelScope.launch {
        when (val result = getSentPinBuddyRequestsUseCase(sentPinBuddyPagination.pageNum, Pagination.DEFAULT_PAGE_SIZE)) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        sentPinBuddyRequests = result.data.pinBuddyRequests,
                    )
                }
            }
        }
    }
}

data class PinBuddyUiState(
    val pinBuddies: List<Profile> = emptyList(),
    val sentPinBuddyRequests: List<PinBuddyRequest> = emptyList(),
    val receivePinBuddyRequests: List<PinBuddyRequest> = emptyList(),
)