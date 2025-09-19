package com.pinup.pinup.ui.article.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ArticleDetailUiState, UiEvent>(ArticleDetailUiState()) {

    companion object {
        private const val ARTICLE_ID = "articleId"

    }

    val articleId = savedStateHandle.get<Int>(ARTICLE_ID) ?: 0

    init {
        getArticleDetail()
    }

    fun getArticleDetail() = viewModelScope.launch {
        //TODO 구현해야 함.
        updateState {
            copy(
                articleList = listOf(
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
                ).filter { it.id != articleId },
                articleDetail = ArticleDetail(
                    id = 10,
                    title = "묵호 1박2일 단기코스",
                    description = "일상을 잠시 멈추고 떠나기 좋은 묵호를 \n" +
                            "소개합니다.",
                    writer = "Editor.핀업",
                    createdAt = "Jun.13 2025",
                    image = listOf(
                        "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/4a614d65-8a13-4f58-bae1-a8b0891180a5.png",
                        "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/a5fd2fd9-2ae5-4df8-a28b-3767f0d45de4.png",
                        "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/profiles/2025/09/08/b46218cb-666e-41d5-b463-bd5cb49e0ff3.png"
                    ),
                    content = "청춘! 이는 듣기만 하여도 가슴이 설레는 말이다. 청춘! 너의 두 손을 가슴에 대고, 물방아 같은 심장의 고동을 들어 보라. 청춘의 피는 끓는다." +
                            "\n열심히 일하고 맞이한 휴일엔 별생각 없이 그저\n" +
                            "걷고 싶어요. 어떤 걸 먹을 지도, 볼지도 모르는 상태로 무작정 만나 성수를 기웃거렸어요.\n" +
                            "일정을 짰다면 절대 찾지 못했을 곳에서 느낀\n" +
                            "그 감정 그대로 소개할게요.",
                    place = listOf(
                        Place(
                            address = "강원 동해시 중앙시장길 24",
                            averageStarRating = 5.0,
                            categoryCode = "",
                            description = "",
                            kakaoPlaceId = "1",
                            latitude = 0.0,
                            longitude = 0.0,
                            name = "어항",
                            placeCategory = "",
                            reviewCount = 1,
                            roadAddress = "강원 동해시 중앙시장길 24",
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/4a614d65-8a13-4f58-bae1-a8b0891180a5.png",
                        ),
                        Place(
                            address = "강원 동해시 일출로 247-7 1층",
                            averageStarRating = 4.0,
                            categoryCode = "",
                            description = "",
                            kakaoPlaceId = "2",
                            latitude = 0.0,
                            longitude = 0.0,
                            name = "짬뽕타운",
                            placeCategory = "",
                            reviewCount = 2,
                            roadAddress = "강원 동해시 일출로 247-7 1층",
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/4a614d65-8a13-4f58-bae1-a8b0891180a5.png",
                        ),
                        Place(
                            address = "강원 동해시 등대오름길 24-5 무코야 선물가게",
                            averageStarRating = 3.0,
                            categoryCode = "",
                            description = "",
                            kakaoPlaceId = "3",
                            latitude = 0.0,
                            longitude = 0.0,
                            name = "무코야 선물가게",
                            placeCategory = "",
                            reviewCount = 3,
                            roadAddress = "강원 동해시 등대오름길 24-5 무코야 선물가게",
                            image = "https://pinup-bucket-dev.s3.ap-northeast-2.amazonaws.com/reviews/2025/09/10/4a614d65-8a13-4f58-bae1-a8b0891180a5.png",
                        )
                    )
                )
            )
        }
    }

}

data class ArticleDetailUiState(
    val articleList: List<ArticlePlace> = emptyList(),
    val articleDetail: ArticleDetail = ArticleDetail()
): UiState