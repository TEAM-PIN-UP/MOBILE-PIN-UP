package com.pinup.pinup.data.request.pints

import kotlinx.serialization.Serializable

@Serializable
data class PageAble(
    val page: Int,
    val size: Int = 20
)
