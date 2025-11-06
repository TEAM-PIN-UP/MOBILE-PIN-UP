package com.pinup.placePinup.data.response

import com.pinup.placePinup.domain.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class SearchPlacesResponse(
    val address: String,
    val averageStarRating: Double,
    val categoryCode: String,
    val description: String,
    val kakaoPlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val placeCategory: String,
    val reviewCount: Int,
    val roadAddress: String,
    val hasMyReview: Boolean
) {
    companion object {
        fun SearchPlacesResponse.toModel(): Place {
            return Place(
                averageStarRating = averageStarRating,
                kakaoPlaceId = kakaoPlaceId,
                latitude = latitude,
                longitude = longitude,
                name = name,
                placeCategory = placeCategory,
                reviewCount = reviewCount,
                address = address,
                categoryCode = categoryCode,
                description = description,
                roadAddress = roadAddress,
                hasMyReview = hasMyReview
            )
        }
    }
}
