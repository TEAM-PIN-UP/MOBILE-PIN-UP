package com.pinup.placePinup.data.response

import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class GetBookmarksResponse(
    val id: Int,
    val kakaoPlaceId: String,
    val placeAddress: String,
    val placeCategory: String,
    val placeFirstReviewImageUrl: String?,
    val placeId: Int,
    val placeLatitude: Double,
    val placeLongitude: Double,
    val placeName: String,
    val placeRoadAddress: String,
    val placeStatus: String
) {
    companion object {
        fun GetBookmarksResponse.toModel(): BookmarkedPlace {
            return BookmarkedPlace(
                id = id,
                kakaoPlaceId = kakaoPlaceId,
                placeAddress = placeAddress,
                placeCategory = Category.of(placeCategory),
                placeFirstReviewImageUrl = placeFirstReviewImageUrl ?: "",
                placeId = placeId,
                placeLatitude = placeLatitude,
                placeLongitude = placeLongitude,
                placeName = placeName,
                placeRoadAddress = placeRoadAddress,
                placeStatus = placeStatus,
            )
        }
    }
}