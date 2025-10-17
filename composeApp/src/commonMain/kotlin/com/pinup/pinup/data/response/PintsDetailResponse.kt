package com.pinup.pinup.data.response

import com.pinup.pinup.data.request.pints.PlaceSummariesRequest
import kotlinx.serialization.Serializable

@Serializable
data class PintsDetailResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val placeSummariesRequest: List<PlaceSummariesRequest> = emptyList()
)
