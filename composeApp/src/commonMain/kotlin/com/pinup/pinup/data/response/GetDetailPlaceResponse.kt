package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.MapPlaceResponse.Companion.toModel
import com.pinup.pinup.data.response.PlaceReviewResponse.Companion.toModel
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.RatingGraph
import com.pinup.pinup.domain.model.PlaceReview
import com.pinup.pinup.domain.model.ReviewedPlace
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GetDetailPlaceResponse(
    val mapPlaceResponse: MapPlaceResponse,
    val ratingGraph: JsonObject,
    val reviews: List<PlaceReviewResponse>
) {
    companion object {
        fun GetDetailPlaceResponse.toModel(): DetailPlace {
            return DetailPlace(
                mapPlace = mapPlaceResponse.toModel(),
                ratingGraph = RatingGraph.toModel(ratingGraph),
                placeReviews = reviews.map {
                    it.toModel()
                }
            )
        }
    }
}

@Serializable
data class MapPlaceResponse(
    val kakaoPlaceId: String,
    val name: String,
    val averageStarRating: Double,
    val reviewCount: Int,
    val distance: String?,
    val latitude: Double,
    val longitude: Double,
    val placeCategory: String,
    val reviewImageUrls: List<String>,
    val reviewerProfileImageUrls: List<String?>,
    val bookmark: Boolean,
) {
    companion object {
        fun MapPlaceResponse.toModel(): ReviewedPlace {
            return ReviewedPlace(
                averageStarRating = averageStarRating,
                bookmark = bookmark,
                distance = distance,
                kakaoPlaceId = kakaoPlaceId,
                latitude = latitude,
                longitude = longitude,
                name = name,
                placeCategory = Category.of(placeCategory),
                reviewCount = reviewCount,
                reviewImageUrls = reviewImageUrls,
                reviewerProfileImageUrls = reviewerProfileImageUrls,
            )
        }
    }
}

@Serializable
data class RatingGraphResponse(
    val additionalProp1: Int,
    val additionalProp2: Int,
    val additionalProp3: Int,
    val additionalProp4: Int,
    val additionalProp5: Int,
)

@Serializable
data class PlaceReviewResponse(
    val content: String,
    val reviewId: Int,
    val reviewImageUrls: List<String>,
    val starRating: Double,
    val visitedDate: String,
    val writerName: String,
    val writerProfileImageUrl: String? = "",
    val writerTotalReviewCount: Int
) {
    companion object {
        fun PlaceReviewResponse.toModel(): PlaceReview {
            return PlaceReview(
                content = this.content,
                reviewId = this.reviewId,
                reviewImageUrls = this.reviewImageUrls,
                starRating = this.starRating,
                visitedDate = this.visitedDate,
                writerName = this.writerName,
                writerProfileImageUrl = this.writerProfileImageUrl,
                writerTotalReviewCount = this.writerTotalReviewCount,
            )
        }
    }
}