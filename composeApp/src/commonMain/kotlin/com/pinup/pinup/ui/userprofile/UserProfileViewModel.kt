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
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.RejectPinBuddyUseCase
import com.pinup.pinup.domain.usecase.RequestPinBuddyUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
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
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val postReviewLikeChangeUseCase : PostReviewLikeChangeUseCase
) : BaseViewModel<UserProfileUiState, UiEvent>(UserProfileUiState()) {
    val memberId = savedStateHandle.get<Int>(MEMBER_ID) ?: 0
    private val photoReviewPagination = Pagination()
    private val textReviewPagination = Pagination()

    init {
        initUserProfile()
    }

    private fun initUserProfile() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase(memberId).getSuccessOrNull() ?: return@launch
        val photoReviews = getPhotoReviewsUseCase(memberId, photoReviewPagination.pageNum, PAGE_SIZE).getSuccessOrNull() ?: return@launch
        val textReviews = getTextReviewsUseCase(memberId, textReviewPagination.pageNum, PAGE_SIZE).getSuccessOrNull() ?: return@launch
        updateState {
            copy(
                member = memberInfo,
                photoReviews = photoReviews.reviews,
                textReviews = textReviews.reviews
            )
        }
    }

    private fun updateUserProfile() = viewModelScope.launch {
        resultResponse(
            response = getMemberInfoUseCase(memberId),
            successCallback = {
                updateState {
                    copy(
                        member = it,
                    )
                }
            }
        )
    }

    fun requestPinBuddy() = viewModelScope.launch {
        resultResponse(
            response = requestPinBuddyUseCase(memberId),
            successCallback = {
                updateUserProfile()
            }
        )
    }

    fun deleteRequestPinBuddy() = viewModelScope.launch {
        resultResponse(
            response = deleteRequestPinBuddyUseCase(memberId),
            successCallback = {
                updateUserProfile()
            }
        )
    }

    fun deletePinBuddy() = viewModelScope.launch {
        resultResponse(
            response = deletePinBuddyUseCase(memberId.toString()),
            successCallback = {
                updateUserProfile()
            }
        )
    }

    fun likeChanged(id: Int, isLike: Boolean) = viewModelScope.launch {
        resultResponse(
            response = postReviewLikeChangeUseCase(id, isLike),
            successCallback = {
                handleSuccessLikeChanged(id)
            }
        )
    }

    private fun handleSuccessLikeChanged(id: Int) = viewModelScope.launch {
        //TODO 수정 예정
//        resultResponse(
//            response = getPhotoReviewsUseCase(id, 1),
//            successCallback = { result ->
//                updateState {
//                    copy(
//                        pagingReview = pagingReview.copy(
//                            reviews = pagingReview.reviews.map {
//                                if (it.id == result.reviews[0].id) {
//                                    result.reviews[0]
//                                } else {
//                                    it
//                                }
//                            }
//                        )
//                    )
//                }
//            }
//        )
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
) : UiState