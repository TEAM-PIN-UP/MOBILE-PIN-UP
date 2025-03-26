package com.pinup.pinup.extentions

import com.naver.maps.geometry.LatLng
import com.pinup.pinup.domain.model.Position

fun Position.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}