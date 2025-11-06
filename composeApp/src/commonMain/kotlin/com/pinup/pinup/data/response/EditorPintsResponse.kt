package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.PinchListItem
import kotlinx.serialization.Serializable

@Serializable
data class EditorPintsResponse(
    val id: Int = -1,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val exposureYn: Boolean = false,
    val clickCnt: Int = 0,
    val imageUrl: String? = "",
    val placeList: List<PlaceItemResponse> = emptyList(),
    val keywordList: List<String> = emptyList(),
    val createdAt: String = "",
) {
    companion object {
        fun EditorPintsResponse.toModel() : PinchListItem {
            return PinchListItem(
                id = id,
                title = title,
                description = description,
                author = author,
                imageUrl = imageUrl,
                keywordList = keywordList,
                createdAt = createdAt
            )
        }
    }
}

@Serializable
data class PlaceItemResponse(
    val kakaoPlaceId: String = "",
    val name: String = "",
    val address: String = "",
    val imageUrl: String = ""
)
