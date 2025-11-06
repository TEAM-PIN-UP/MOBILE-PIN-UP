package com.pinup.placePinup.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.domain.model.Member
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.model.Profile
import com.pinup.placePinup.domain.model.RelationType
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.usecase.AcceptPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.DeletePinBuddyUseCase
import com.pinup.placePinup.domain.usecase.DeleteRequestPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.GetFeedUseCase
import com.pinup.placePinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.GetPintsUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.domain.usecase.RejectPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.RequestPinBuddyUseCase
import com.pinup.placePinup.domain.usecase.SearchUserUseCase
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.login.sns.KaKaoShareController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserProfileViewModel (
    savedStateHandle: SavedStateHandle,
    private val contextFactory: ContextFactory,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val requestPinBuddyUseCase: RequestPinBuddyUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val deleteRequestPinBuddyUseCase: DeleteRequestPinBuddyUseCase,
    private val deletePinBuddyUseCase: DeletePinBuddyUseCase,
    private val postReviewLikeChangeUseCase : PostReviewLikeChangeUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getPintsUseCase: GetPintsUseCase,
    private val acceptPinBuddyUseCase: AcceptPinBuddyUseCase,
    private val rejectPinBuddyUseCase: RejectPinBuddyUseCase,
    private val kaKaoShareController: KaKaoShareController
) : BaseViewModel<UserProfileUiState, UiEvent>(UserProfileUiState()) {
    val memberId = savedStateHandle.get<Int>(MEMBER_ID) ?: -1
    val memberName = savedStateHandle.get<String>(MEMBER_NAME) ?: ""

    init {
        if(memberId != -1) initUserProfile(memberId) else getUserId()
        getPintsList()
        getMyInfo()
    }

    private fun initUserProfile(memberId: Int) = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase(memberId).getSuccessOrNull() ?: return@launch
        getFeedList(memberId = memberId)
        updateState {
            copy(
                member = memberInfo,
            )
        }
    }

    private fun getUserId() = viewModelScope.launch {
        resultResponse(
            response = searchUserUseCase(memberName),
            successCallback = {
                initUserProfile(it[0].profile.memberId)
            }
        )
    }

    private fun getMyInfo() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest {
                updateState {
                    copy(
                        profileUrl = it.profileUrl,
                    )
                }
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

    private fun getPintsList() = viewModelScope.launch {
        val pageAble = PageAble(
            page = uiState.value.nowPage
        )
        resultResponse(
            response = getPintsUseCase(uiState.value.member.profile.memberId, pageAble),
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

    fun shareMyProfile(){
        kaKaoShareController.kakaoShare(
            context = contextFactory.getActivity(),
            memberId = uiState.value.member.profile.memberId,
            memberName = uiState.value.member.profile.nickname,
        )
    }

    fun acceptPinBuddy() = viewModelScope.launch {
        resultResponse(
            response = acceptPinBuddyUseCase(uiState.value.member.profile.memberId),
            successCallback = {
                initUserProfile(uiState.value.member.profile.memberId)
            }
        )
    }

    fun rejectPinBuddy() = viewModelScope.launch {
        resultResponse(
            response = rejectPinBuddyUseCase(uiState.value.member.profile.memberId),
            successCallback = {
                initUserProfile(uiState.value.member.profile.memberId)
            }
        )
    }

    companion object {
        private const val MEMBER_ID = "memberId"
        private const val MEMBER_NAME = "name"
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
    val prevCursor: Int? = null,
    val pinchPageAble: PintsPageAble = PintsPageAble(),
    val nowPage: Int = 1,
    val profileUrl: String = "",
) : UiState