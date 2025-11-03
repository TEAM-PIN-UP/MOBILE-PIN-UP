package com.pinup.pinup.data.request.pints

import com.pinup.pinup.domain.model.PintsCategory
import com.pinup.pinup.domain.model.SortType
import kotlinx.serialization.Serializable

@Serializable
data class GetEditorPintsRequest(
    val swLatitude: String,
    val swLongitude: String,
    val neLatitude: String,
    val neLongitude: String,
    val currentLatitude: String = "",
    val currentLongitude: String = "",
    val sort: SortType = SortType.LATEST,
    val category: PintsCategory = PintsCategory.ALL,
)