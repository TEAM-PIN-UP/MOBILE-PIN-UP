package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
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
) {
    companion object {
        fun MemberResponse.toModel(): Profile {
            return Profile(
                bio = bio,
                email = email,
                memberId = memberId,
                name = name,
                nickname = nickname,
                profilePictureUrl = profilePictureUrl,
                termsOfMarketing = termsOfMarketing,
                averageStarRating = averageStarRating,
                pinBuddyCount = pinBuddyCount,
                reviewCount = reviewCount,
            )
        }
    }
}