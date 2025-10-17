package com.pinup.pinup.data.response

import com.pinup.pinup.data.request.pints.PlaceSummariesRequest
import com.pinup.pinup.domain.model.PinchPlaceItem
import com.pinup.pinup.domain.model.PintsDetail
import kotlinx.serialization.Serializable

@Serializable
data class PintsDetailResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val placeSummaries: List<PlaceSummariesRequest> = emptyList()
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
