package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.EditorPintsPlaceResponse.Companion.toModel
import com.pinup.placePinup.data.response.ReviewImageResponse.Companion.toModel
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.EditorPintsPlace
import com.pinup.placePinup.domain.model.ReviewImage
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class EditorPintsDetailResponse(
    val id: Int = -1,
    val title: String = "",
    val content: String = "",
    val pintsPlaceList: List<EditorPintsPlaceResponse> = emptyList()
){
    companion object {
        fun EditorPintsDetailResponse.toModel() : EditorPintsDetail {
            return EditorPintsDetail(
                id = id,
                title = title,
                content = content,
                pintsPlaceList = pintsPlaceList.map {
                    it.toModel()
                }
            )
        }
    }
}

@Serializable
data class EditorPintsPlaceResponse(
    val id: Int = -1,
    val kakaoPlaceId: String = "",
    val name: String = "",
    val address: String = "",
    val averageStarRating: Double = 0.0,
    val reviewCount: Int = 0,
    val distant: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val pintsPlaceCategory: String = "",
    val reviewImages: List<ReviewImageResponse> = emptyList(),
    val reviewerProfileImages: List<ReviewImageResponse> = emptyList(),
    val bookmark: Boolean = false,
) {
    companion object {
        fun EditorPintsPlaceResponse.toModel() : EditorPintsPlace {
            return EditorPintsPlace(
                id = id,
                kakaoPlaceId = kakaoPlaceId,
                name = name,
                address = address,
                averageStarRating = averageStarRating,
                reviewCount = reviewCount,
                distant = distant,
                latitude = latitude,
                longitude = longitude,
                pintsPlaceCategory = Category.of(pintsPlaceCategory),
                reviewImages = reviewImages.map {
                    it.toModel()
                },
                reviewerProfileImages = reviewerProfileImages.map {
                    it.toModel()
                },
                bookmark = bookmark,
            )
        }
    }
}

@Serializable
data class ReviewImageResponse(
    val id: Int = -1,
    val url: String = "",
) {
    companion object{
        fun ReviewImageResponse.toModel() : ReviewImage{
            return ReviewImage(
                id = id,
                url = url
            )
        }
    }
}