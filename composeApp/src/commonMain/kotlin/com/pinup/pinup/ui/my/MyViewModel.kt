package com.pinup.pinup.ui.my

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.GetBookmarksUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPhotoReviewsUseCase
import com.pinup.pinup.domain.usecase.GetTextReviewsUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.platform.kakaoShare
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.model.ChipState
import kotlinx.coroutines.launch
import kotlin.collections.plus
import kotlin.text.ifEmpty


class MyViewModel (
    private val contextFactory: ContextFactory,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
) : BaseViewModel<MyUiState, UiEvent>(MyUiState()) {

    fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        getFeedList(memberId = memberInfo.profile.memberId)
        getScrapList()
        updateState {
            copy(
                member = memberInfo,
            )
        }
    }

    fun getFeedList(id: Int? = uiState.value.pagingReview.nextCursor, memberId : Int) = viewModelScope.launch {
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

    fun deleteReview(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(id),
            successCallback = {
                updateState {
                    copy(
                        pagingReview = pagingReview.copy(
                            reviews = pagingReview.reviews.filter { it.id != id }
                        )
                    )
                }
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
            response = getFeedUseCase(id, 1, memberId = uiState.value.member.profile.memberId, null),
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

    fun shareMyProfile(){
        kakaoShare(
            context = contextFactory.getActivity(),
            memberId = uiState.value.member.profile.memberId,
            memberName = uiState.value.member.profile.name
        )
    }

    private fun getScrapList() = viewModelScope.launch {
        resultResponse(
            response = getBookmarksUseCase(
                sort = SortType.LATEST,
                category = Category.ALL,
                currentLatitude = "",
                currentLongitude = "",
            ),
            successCallback = {
                updateState {
                    copy(
                        scrapList = it
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
    val pagingReview: PagingReview = PagingReview(),
    val prevCursor: Int? = null,
    val scrapList: List<BookmarkedPlace> = emptyList()
) : UiState