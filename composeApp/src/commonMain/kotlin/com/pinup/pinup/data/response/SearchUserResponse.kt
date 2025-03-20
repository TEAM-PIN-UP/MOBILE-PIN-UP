package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.MemberResponse.Companion.toModel
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.model.RelationType
import kotlinx.serialization.Serializable

@Serializable
data class SearchUserResponse(
    val memberResponse: MemberResponse,
    val relationType: String,
) {
    companion object {
        fun SearchUserResponse.toModel(): PinBuddy {
            return PinBuddy(
                profile = memberResponse.toModel(),
                relationType = RelationType.of(relationType),
                friendRequestId = null,
            )
        }
    }
}