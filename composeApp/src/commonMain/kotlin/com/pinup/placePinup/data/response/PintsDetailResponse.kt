package com.pinup.placePinup.data.response

import com.pinup.placePinup.domain.model.PinchPlaceItem
import com.pinup.placePinup.domain.model.PintsDetail
import kotlinx.serialization.Serializable

@Serializable
data class PintsDetailResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val placeSummaries: List<PlaceSummariesResponse> = emptyList()
) {
    companion object {
        fun PintsDetailResponse.toModel() : PintsDetail {
            return PintsDetail(
                pintsId = pintsId,
                title = title,
                content = content,
                createdAt = createdAt,
                placeSummaries = placeSummaries.map {
                    PinchPlaceItem(
                        kakaoPlaceId = it.kakaoPlaceId,
                        name = it.name,
                        address = it.address,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            )
        }
    }
}

@Serializable
data class PlaceSummariesResponse(
    val kakaoPlaceId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
