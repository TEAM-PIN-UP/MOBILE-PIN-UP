package com.pinup.pinup.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.DeletePinBuddyUseCase
import com.pinup.pinup.domain.usecase.DeleteRequestPinBuddyUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.RequestPinBuddyUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class UserProfileViewModel (
    savedStateHandle: SavedStateHandle,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val requestPinBuddyUseCase: RequestPinBuddyUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val postReviewLikeChangeUseCase : PostReviewLikeChangeUseCase
) : BaseViewModel<UserProfileUiState, UiEvent>(UserProfileUiState()) {
    val memberId = savedStateHandle.get<Int>(MEMBER_ID) ?: 0

    init {
        initUserProfile()
    }

    private fun initUserProfile() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase(memberId).getSuccessOrNull() ?: return@launch
        getFeedList(memberId = memberId)
        updateState {
            copy(
                member = memberInfo,
            )
        }
    }

    fun getFeedList(id: Int = uiState.value.pagingReview.nextCursor, memberId : Int) = viewModelScope.launch {
        resultResponse(
            response = getFeedUseCase(id, memberId = memberId, keyword = null),
            successCallback = {
                updateState {
                    copy(
                        prevCursor = id,
                        pagingReview = it.copy(
                            reviews = pagingReview.reviews + it.reviews
                        ),
                    )
                }
            }
        )
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
        resultResponse(
            response = getFeedUseCase(id, 1, memberId = memberId, keyword = null),
            successCallback = { result ->
                updateState {
                    copy(
                        pagingReview = pagingReview.copy(
                            reviews = pagingReview.reviews.map {
                                if (it.id == result.reviews[0].id) {
                                    result.reviews[0]
                                } else {
                                    it
                                }
                            }
                        )
                    )
                }
            }
        )
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
    val pagingReview: PagingReview = PagingReview(),
    val prevCursor: Int = 0,
) : UiState