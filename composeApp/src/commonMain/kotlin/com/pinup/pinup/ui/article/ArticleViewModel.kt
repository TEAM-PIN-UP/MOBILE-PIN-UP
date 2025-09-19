package com.pinup.pinup.ui.article

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.DeleteRecentSearchUseCase
import com.pinup.pinup.domain.usecase.GetFeedUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.GetRecentSearchUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.SaveRecentSearchUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase,
) : BaseViewModel<ArticleUiState, UiEvent>(ArticleUiState()) {

    init {
        getMyProfile()
    }

    fun getArticleList() = viewModelScope.launch {
        //TODO 구현해야 함.
        updateState {
            copy(
                pagingArticle = PagingArticle(
                    articles = listOf(
                        ArticlePlace(
                            id = 0,
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/4a614d65-8a13-4f58-bae1-a8b0891180a5.png",
                            title = "합정역 아인슈페너 맛집 16곳",
                            createdAt = "Jun 13. 2025",
                            description = "아 몰랑 이게 설명글이야"
                        ),
                        ArticlePlace(
                            id = 1,
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/a5fd2fd9-2ae5-4df8-a28b-3767f0d45de4.png",
                            title = "미정이이야",
                            createdAt = "Jun 13. 2025",
                            description = "아 몰랑 이게 설명글이야"
                        ),
                        ArticlePlace(
                            id = 2,
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/profiles/2025/09/08/b46218cb-666e-41d5-b463-bd5cb49e0ff3.png",
                            title = "강남에서 찾은 프랑스의 맛",
                            createdAt = "Jun 13. 2025",
                            description = "아 몰랑 이게 설명글이야"
                        ),
                        ArticlePlace(
                            id = 3,
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/871a54ca-be8b-4881-af75-6e37e8c21a57.png",
                            title = "묵호 1박2일 단기코스",
                            createdAt = "Jun 13. 2025",
                            description = "아 몰랑 이게 설명글이야"
                        ),
                    )
                )
            )
        }
    }

    private fun getMyProfile() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest {
                updateState {
                    copy(
                        profileUrl = it.profileUrl
                    )
                }
            }
    }

}

data class ArticleUiState(
    val pagingArticle: PagingArticle = PagingArticle(),
    val prevCursor: Int? = null,
    val profileUrl: String = "",
): UiState