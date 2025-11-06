package com.pinup.placePinup.domain.model

enum class SortType(val text: String) {
    NEAR("가까운 순"),
    LATEST("최신 순"),
    STAR_HIGH("별점 높은 순"),
    STAR_LOW("별점 낮은 순")
}