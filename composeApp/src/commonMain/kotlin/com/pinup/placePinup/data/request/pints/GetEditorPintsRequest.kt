package com.pinup.placePinup.data.request.pints

import com.pinup.placePinup.domain.model.PintsCategory
import com.pinup.placePinup.domain.model.SortType
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