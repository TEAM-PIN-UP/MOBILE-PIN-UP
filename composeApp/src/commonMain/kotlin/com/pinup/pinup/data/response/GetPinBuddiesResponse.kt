package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.MemberResponse.Companion.toModel
import com.pinup.pinup.domain.model.PagingPinBuddy
import kotlinx.serialization.Serializable

@Serializable
data class GetPinBuddiesResponse(
    val content: List<MemberResponse>,
    val totalElements: Int,
    val totalPages: Int
) {
    companion object {
        fun GetPinBuddiesResponse.toModel() : PagingPinBuddy {
            return PagingPinBuddy(
                profiles = content.map {
                    it.toModel()
                },
                totalPages = totalPages,
                totalElements = totalElements
            )
        }
    }
}