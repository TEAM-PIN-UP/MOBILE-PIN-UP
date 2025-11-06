package com.pinup.placePinup.domain.model

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Position(
    val latitude: Double,
    val longitude: Double
) {
    val isValid
        get() = this.latitude.isNaN().not() && this.longitude.isNaN().not() && this.longitude.isInfinite().not() && this.latitude.isInfinite().not()
    companion object {
        val INVALID = Position(Double.NaN, Double.NaN)

        private const val EARTH_R = 6371000.0

        fun Position.distanceMetersTo(other: Position): Double {
            fun Double.toRad() = this * PI / 180.0
            val dLat = (other.latitude - latitude).toRad()
            val dLon = (other.longitude - longitude).toRad()
            val a = sin(dLat / 2).pow(2) +
                    cos(latitude.toRad()) * cos(other.latitude.toRad()) *
                    sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return EARTH_R * c
        }

        fun Position.near(other: Position, meters: Double = 0.5): Boolean =
            distanceMetersTo(other) <= meters
    }
}