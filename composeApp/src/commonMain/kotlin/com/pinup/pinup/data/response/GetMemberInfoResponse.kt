package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.MemberResponse.Companion.toModel
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.RelationType
import kotlinx.serialization.Serializable

@Serializable
data class GetMemberInfoResponse(
    val memberResponse: MemberResponse,
    val relationType: String,
    val friendRequestId: Int?
) {
    companion object {
        fun GetMemberInfoResponse.toModel() : Member {
            return Member(
                profile = memberResponse.toModel(),
                relationType = RelationType.of(relationType),
                friendRequestId = friendRequestId
            )
        }
    }
}