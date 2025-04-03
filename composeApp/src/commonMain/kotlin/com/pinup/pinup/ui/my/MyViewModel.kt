package com.pinup.pinup.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Pagination
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.getSuccessOrNull
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPhotoReviewsUseCase
import com.pinup.pinup.domain.usecase.GetTextReviewsUseCase
import com.pinup.pinup.platform.hLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MyViewModel (
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getPhotoReviewsUseCase: GetPhotoReviewsUseCase,
    private val getTextReviewsUseCase: GetTextReviewsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState>
        get() = _uiState.asStateFlow()
    private val photoReviewPagination = Pagination()
    private val textReviewPagination = Pagination()

    init {
        initMyInfo()
    }

    private fun initMyInfo() = viewModelScope.launch {
        val memberInfo = getMemberInfoUseCase().getSuccessOrNull() ?: return@launch
        val photoReviews = getPhotoReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull() ?: return@launch
        val textReviews = getTextReviewsUseCase(
            page = photoReviewPagination.pageNum,
            size = Pagination.DEFAULT_PAGE_SIZE
        ).getSuccessOrNull() ?: return@launch
        photoReviewPagination.totalPage = photoReviews.totalPages
        textReviewPagination.totalPage = textReviews.totalPages
        _uiState.update {
            it.copy(
                member = memberInfo,
                photoReviews = photoReviews.reviews,
                textReviews = textReviews.reviews
            )
        }
    }

    fun updateProfile() = viewModelScope.launch {
        when (val result = getMemberInfoUseCase()) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        member = result.data
                    )
                }
            }
        }
    }

    fun getPhotoReviews() = viewModelScope.launch {
        if (photoReviewPagination.isLast) return@launch
        when (val result = getPhotoReviewsUseCase(
            page = photoReviewPagination.nextPage(),
            size = Pagination.DEFAULT_PAGE_SIZE
        )) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        photoReviews = it.photoReviews + result.data.reviews,
                    )
                }
            }
        }
    }

    fun getTextReviews() = viewModelScope.launch {
        if (textReviewPagination.isLast) return@launch
        when (val result = getTextReviewsUseCase(
            page = photoReviewPagination.nextPage(),
            size = Pagination.DEFAULT_PAGE_SIZE)
        ) {
            is PResult.Fail -> {
                hLog("result >>> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        textReviews = it.textReviews + result.data.reviews,
                    )
                }
            }
        }
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
)