package com.pinup.pinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class EditorPintsResponse(
    val id: Int = -1,
    val title: String = "",
    val author: String = "",
    val exposureYn: Boolean = false,
    val clickCnt: Int = 0,
    val imageUrl: String? = "",
    val placeList: List<PlaceItem> = emptyList(),
    val keywordList: String = ""
)

@Serializable
data class PlaceItem(
    val kakaoPlaceId: String = "",
    val name: String = "",
    val address: String = "",
    val imageUrl: String = ""
)
