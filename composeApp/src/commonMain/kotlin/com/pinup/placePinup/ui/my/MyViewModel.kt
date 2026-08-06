package com.pinup.placePinup.ui.my

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.Member
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.model.Profile
import com.pinup.placePinup.domain.model.RelationType
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.usecase.DeletePinlogUseCase
import com.pinup.placePinup.domain.usecase.GetBookmarksUseCase
import com.pinup.placePinup.domain.usecase.GetFeedUseCase
import com.pinup.placePinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.placePinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.placePinup.domain.usecase.GetPintsUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.event.DetailPlaceEventBus
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.login.sns.KaKaoShareController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.plus
import kotlin.time.Duration.Companion.seconds


class MyViewModel (
    private val contextFactory: ContextFactory,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
    private val getPintsUseCase: GetPintsUseCase,
    private val kaKaoShareController: KaKaoShareController
) : BaseViewModel<MyUiState, UiEvent>(MyUiState()) {

    fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        getFeedList(id = null, memberId = memberInfo.profile.memberId)
        getScrapList()
        getPintsList(memberId = memberInfo.profile.memberId)
        updateState {
            copy(
                member = memberInfo,
                isRefreshing = false
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
                            reviews = if (id == null) it.reviews else pagingReview.reviews + it.reviews
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

    // 마이 프로필 핀로그 장소 뱃지 탭 -> 핀맵 탭으로 이동해 장소 상세 표시(내 리뷰라 게이트는 통과).
    fun moveToPlaceOnMap(kakaoPlaceId: String, reviewId: Int) = viewModelScope.launch {
        DetailPlaceEventBus.sendEvent(kakaoPlaceId, reviewId)
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
            response = getPinlogDetailUseCase(id),
            successCallback = { result ->
                updateState {
                    copy(
                        pagingReview = pagingReview.copy(
                            reviews = pagingReview.reviews.map {
                                if (it.id == id) {
                                    it.copy(
                                        isLikedByUser = result.isLikedByUser,
                                        likeCount = result.likeCount
                                    )
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

    fun shareMyProfile(shareTitle: String, shareContent: String, shareButton: String) {
        kaKaoShareController.kakaoShare(
            context = contextFactory.getActivity(),
            memberId = uiState.value.member.profile.memberId,
            memberName = uiState.value.member.profile.nickname,
            shareTitle = shareTitle,
            shareContent = shareContent,
            shareButton = shareButton,
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

    private fun getPintsList(memberId: Int) = viewModelScope.launch {
        val pageAble = PageAble()
        resultResponse(
            response = getPintsUseCase(memberId, pageAble),
            successCallback = {
                updateState {
                    copy(
                        pinchPageAble = it,
                        nowPage = nowPage + 1
                    )
                }
            }
        )
    }

    fun getMorePints() = viewModelScope.launch {
        val pageAble = PageAble(
            page = uiState.value.nowPage
        )
        resultResponse(
            response = getPintsUseCase(uiState.value.member.profile.memberId, pageAble),
            successCallback = {
                updateState {
                    copy(
                        pinchPageAble = it.copy(
                            content = uiState.value.pinchPageAble.content + it.content
                        ),
                        nowPage = nowPage + 1
                    )
                }
            }
        )
    }

    fun refreshView() = viewModelScope.launch{
        updateState {
            copy(
                isRefreshing = true
            )
        }
        delay(1.seconds)
        initMyInfo()
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
    val scrapList: List<BookmarkedPlace> = emptyList(),
    val pinchPageAble: PintsPageAble = PintsPageAble(),
    val nowPage: Int = 1,
    val isRefreshing: Boolean = false
) : UiState