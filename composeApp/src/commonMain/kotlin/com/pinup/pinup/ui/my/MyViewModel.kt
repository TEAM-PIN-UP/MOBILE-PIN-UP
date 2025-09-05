package com.pinup.pinup.ui.my

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPhotoReviewsUseCase
import com.pinup.pinup.domain.usecase.GetTextReviewsUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class MyViewModel (
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getPhotoReviewsUseCase: GetPhotoReviewsUseCase,
    private val getTextReviewsUseCase: GetTextReviewsUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
) : BaseViewModel<MyUiState, UiEvent>(MyUiState()) {
    private val photoReviewPagination = Pagination()
    private val textReviewPagination = Pagination()

    fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        val photoReviews = getPhotoReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull()
        val textReviews = getTextReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull()
        photoReviewPagination.totalPage = photoReviews?.nextCursor
        textReviewPagination.totalPage = textReviews?.nextCursor
        updateState {
            copy(
                member = memberInfo,
                photoReviews = photoReviews?.reviews ?: emptyList(),
                textReviews = textReviews?.reviews ?: emptyList()
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

    fun deleteReview(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(id),
            successCallback = {
                getPhotoReviews()
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