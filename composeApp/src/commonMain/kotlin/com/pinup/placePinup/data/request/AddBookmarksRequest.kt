package com.pinup.placePinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class AddBookmarksRequest(
    val kakaoPlaceId: String
)
