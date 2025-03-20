package com.pinup.pinup.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.AcceptPinBuddyUseCase
import com.pinup.pinup.domain.usecase.DeletePinBuddyUseCase
import com.pinup.pinup.domain.usecase.DeleteRequestPinBuddyUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPhotoReviewsUseCase
import com.pinup.pinup.domain.usecase.GetTextReviewsUseCase
import com.pinup.pinup.domain.usecase.RejectPinBuddyUseCase
import com.pinup.pinup.domain.usecase.RequestPinBuddyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserProfileViewModel (
    savedStateHandle: SavedStateHandle,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getPhotoReviewsUseCase: GetPhotoReviewsUseCase,
    private val getTextReviewsUseCase: GetTextReviewsUseCase,
    private val requestPinBuddyUseCase: RequestPinBuddyUseCase,
    private val rejectPinBuddyUseCase: RejectPinBuddyUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val acceptPinBuddyUseCase: AcceptPinBuddyUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
) : ViewModel() {
    val memberId = savedStateHandle.get<Int>(MEMBER_ID) ?: 0
    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState>
        get() = _uiState.asStateFlow()
    private val photoReviewPagination = Pagination()
    private val textReviewPagination = Pagination()

    init {
        initUserProfile()
    }

    private fun initUserProfile() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase(memberId).getSuccessOrNull() ?: return@launch
        val photoReviews = getPhotoReviewsUseCase(memberId, photoReviewPagination.pageNum, PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val textReviews = getTextReviewsUseCase(memberId, textReviewPagination.pageNum, PAGE_SIZE).getSuccessOrNull() ?: return@launch
        _uiState.update {
            it.copy(
                member = memberInfo,
                photoReviews = photoReviews.reviews,
                textReviews = textReviews.reviews
            )
        }
    }

    private fun updateUserProfile() = viewModelScope.launch {
        when (val result = getMemberInfoUseCase(memberId)) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        member = result.data,
                    )
                }
            }
        }
    }

    fun requestPinBuddy(memberId: Int) = viewModelScope.launch {
        when (val result = requestPinBuddyUseCase(memberId)) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                updateUserProfile()
            }
        }
    }

    fun deleteRequestPinBuddy(memberId: Int) = viewModelScope.launch {
        when (val result = deleteRequestPinBuddyUseCase(memberId)) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                updateUserProfile()
            }
        }
    }

    fun deletePinBuddy(memberId: Int) = viewModelScope.launch {
        when (val result = deletePinBuddyUseCase(memberId.toString())) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                updateUserProfile()
            }
        }
    }

    companion object {
        private const val MEMBER_ID = "memberId"
        private const val PAGE_SIZE = 20

    }
}

data class UserProfileUiState(
    val member: Member = Member(
        profile = Profile(
            bio = "",
            email = "",
            memberId = 0,
            name = "",
            nickname = "",
            profilePictureUrl = "",
            termsOfMarketing = "",
            averageStarRating = 0.0,
            reviewCount = 0,
            pinBuddyCount = 0,
        ),
        relationType = RelationType.SELF,
        friendRequestId = null
    ),
    val textReviews: List<Review> = emptyList(),
    val photoReviews: List<Review> = emptyList()
)