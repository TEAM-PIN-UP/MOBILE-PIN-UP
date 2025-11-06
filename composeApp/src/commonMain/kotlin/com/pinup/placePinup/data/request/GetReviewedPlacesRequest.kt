package com.pinup.placePinup.data.request

import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.LocationBound
import com.pinup.placePinup.domain.model.SortType
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewedPlacesRequest(
    val swLatitude: String,
    val swLongitude: String,
    val neLatitude: String,
    val neLongitude: String,
    val currentLatitude: String = "",
    val currentLongitude: String = "",
    val sort: SortType = SortType.LATEST,
    val category: Category = Category.ALL,
) {
    companion object {
        fun of(locationBound: LocationBound, currentPosition: Position?, sort: SortType, category: Category): GetReviewedPlacesRequest {
            return GetReviewedPlacesRequest(
                swLatitude = locationBound.swLatitude,
                swLongitude = locationBound.swLongitude,
                neLatitude = locationBound.neLatitude,
                neLongitude = locationBound.neLongitude,
                currentLatitude = currentPosition?.latitude?.toString() ?: "",
                currentLongitude = currentPosition?.longitude?.toString() ?: "",
                sort = sort,
                category = category
            )
        }
    }
}
