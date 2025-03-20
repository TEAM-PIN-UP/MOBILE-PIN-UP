package com.pinup.pinup.data.request

import com.naver.maps.geometry.LatLng
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.SortType
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewedPlacesRequest(
    val swLatitude: String,
    val swLongitude: String,
    val neLatitude: String,
    val neLongitude: String,
    val currentLatitude: String,
    val currentLongitude: String,
    val sort: SortType,
    val category: Category,
) {
    companion object {
        fun of(locationBound: LocationBound, currentLatLng: LatLng?, sort: SortType, category: Category): GetReviewedPlacesRequest {
            return GetReviewedPlacesRequest(
                swLatitude = locationBound.swLatitude,
                swLongitude = locationBound.swLongitude,
                neLatitude = locationBound.neLatitude,
                neLongitude = locationBound.neLongitude,
                currentLatitude = currentLatLng?.latitude?.toString() ?: "",
                currentLongitude = currentLatLng?.longitude?.toString() ?: "",
                sort = sort,
                category = category
            )
        }
    }
}
