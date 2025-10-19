package com.pinup.pinup.data.request.pints

import kotlinx.serialization.Serializable

@Serializable
data class ModifyPintsRequest(
    val title: String,
    val content: String,
    val kakaoPlaceIds: List<String>
)