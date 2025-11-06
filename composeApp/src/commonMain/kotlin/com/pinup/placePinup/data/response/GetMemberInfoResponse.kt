package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.MemberResponse.Companion.toModel
import com.pinup.placePinup.domain.model.Member
import com.pinup.placePinup.domain.model.RelationType
import kotlinx.serialization.Serializable

@Serializable
data class GetMemberInfoResponse(
    val memberResponse: MemberResponse,
    val relationType: String,
    val friendRequestId: Int?,
    val pinBuddyCount: Int?
) {
    companion object {
        fun GetMemberInfoResponse.toModel() : Member {
            return Member(
                profile = memberResponse.copy(
                    pinBuddyCount = pinBuddyCount
                ).toModel(),
                relationType = RelationType.of(relationType),
                friendRequestId = friendRequestId
            )
        }
    }
}