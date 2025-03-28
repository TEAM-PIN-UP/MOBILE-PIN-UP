package com.pinup.pinup.data.request

import com.pinup.pinup.domain.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class PlaceRequest(
    val address: String,
    val category: String,
    val kakaoPlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val roadAddress: String
) {
    companion object {
        fun of(place: Place): PlaceRequest {
            return PlaceRequest(
                address = place.address,
                category = place.placeCategory,
                kakaoPlaceId = place.kakaoPlaceId,
                latitude = place.latitude,
                longitude = place.longitude,
                name = place.name,
                roadAddress = place.roadAddress,
            )
        }
    }
}