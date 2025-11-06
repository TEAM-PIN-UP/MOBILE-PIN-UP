package com.pinup.placePinup.domain.model

data class Profile(
    val bio: String,
    val email: String,
    val memberId: Int,
    val name: String,
    val nickname: String,
    val profilePictureUrl: String?,
    val termsOfMarketing: String,
    val averageStarRating: Double,
    val pinBuddyCount: Int,
    val reviewCount: Int,
)