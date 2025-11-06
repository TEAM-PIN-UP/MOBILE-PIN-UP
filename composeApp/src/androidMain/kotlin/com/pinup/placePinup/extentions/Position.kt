package com.pinup.placePinup.extentions

import com.naver.maps.geometry.LatLng
import com.pinup.placePinup.domain.model.Position

fun Position.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}