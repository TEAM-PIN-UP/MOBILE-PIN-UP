package com.pinup.placePinup.data.request.pints

import kotlinx.serialization.Serializable

@Serializable
data class PageAble(
    val page: Int = 1,
    val size: Int = 20
)
