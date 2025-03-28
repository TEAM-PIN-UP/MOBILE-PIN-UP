package com.pinup.pinup.domain.model

data class Position(
    val latitude: Double,
    val longitude: Double
) {
    val isValid
        get() = this.latitude.isNaN().not() && this.longitude.isNaN().not() && this.longitude.isInfinite().not() && this.latitude.isInfinite().not()
    companion object {
        val INVALID = Position(Double.NaN, Double.NaN)
    }
}