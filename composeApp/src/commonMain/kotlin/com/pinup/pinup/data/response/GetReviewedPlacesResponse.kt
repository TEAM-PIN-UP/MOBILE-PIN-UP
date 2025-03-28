package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.ReviewedPlace
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewedPlacesResponse(
    val averageStarRating: Double,
    val bookmark: Boolean,
    val distance: String?,
    val kakaoPlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val placeCategory: String,
    val reviewCount: Int,
    val reviewImageUrls: List<String>,
    val reviewerProfileImageUrls: List<String?>
) {
    companion object {
        fun GetReviewedPlacesResponse.toModel(): ReviewedPlace {
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