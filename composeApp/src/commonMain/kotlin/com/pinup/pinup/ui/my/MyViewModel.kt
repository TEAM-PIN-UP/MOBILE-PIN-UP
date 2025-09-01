package com.pinup.pinup.ui.my

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPhotoReviewsUseCase
import com.pinup.pinup.domain.usecase.GetTextReviewsUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class MyViewModel (
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getPhotoReviewsUseCase: GetPhotoReviewsUseCase,
    private val getTextReviewsUseCase: GetTextReviewsUseCase,
) : BaseViewModel<MyUiState, UiEvent>(MyUiState()) {
    private val photoReviewPagination = Pagination()
    private val textReviewPagination = Pagination()

    fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        val photoReviews = getPhotoReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull() ?: return@launch
        val textReviews = getTextReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull() ?: return@launch
        photoReviewPagination.totalPage = photoReviews.nextCursor
        textReviewPagination.totalPage = textReviews.nextCursor
        updateState {
            copy(
                member = memberInfo,
                photoReviews = photoReviews.reviews,
                textReviews = textReviews.reviews
            )
        }
    }

    fun updateProfile() = viewModelScope.launch {
        resultResponse(
            response = getMemberInfoUseCase(),
            successCallback = {
                updateState {
                    copy(
                        member = it
                    )
                }
            }
        )
    }

    fun getPhotoReviews() = viewModelScope.launch {
        if (photoReviewPagination.isLast) return@launch
        resultResponse(
            response = getPhotoReviewsUseCase(
                page = photoReviewPagination.nextPage(),
                size = Pagination.DEFAULT_PAGE_SIZE
            ),
            successCallback = {
                updateState {
                    copy(
                        photoReviews = photoReviews + it.reviews,
                    )
                }
            }
        )
    }

    fun getTextReviews() = viewModelScope.launch {
        if (textReviewPagination.isLast) return@launch
        resultResponse(
            response = getTextReviewsUseCase(
                page = photoReviewPagination.nextPage(),
                size = Pagination.DEFAULT_PAGE_SIZE
            ),
            successCallback = {
                updateState {
                    copy(
                        textReviews = textReviews + it.reviews,
                    )
                }
            }
        )
    }
}

data class MyUiState(
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
            pinBuddyCount = 0,
            reviewCount = 0
        ),
        relationType = RelationType.NONE,
        friendRequestId = null
    ),
    val textReviews: List<Review> = emptyList(),
    val photoReviews: List<Review> = emptyList()
) : UiState