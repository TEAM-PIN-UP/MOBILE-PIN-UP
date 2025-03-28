package com.pinup.pinup.domain.model

data class CameraState(
    val isMoving: Boolean,
    val contentBounds: PositionBounds,
    val reason: Reason,
    val position: Position
) {
    enum class Reason {
        GESTURE, ELSE
    }
}
