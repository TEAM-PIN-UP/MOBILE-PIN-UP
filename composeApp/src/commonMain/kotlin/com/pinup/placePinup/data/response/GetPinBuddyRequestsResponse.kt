package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.GetPinBuddyRequestsResponse.Content.Companion.toModel
import com.pinup.placePinup.data.response.MemberResponse.Companion.toModel
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.domain.model.PinBuddyRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetPinBuddyRequestsResponse(
    val content: List<Content>,
    val totalElements: Int,
    val totalPages: Int,
) {
    @Serializable
    data class Content(
        val friendRequestStatus: String,
        val id: Int,
        val receiver: MemberResponse,
        val sender: MemberResponse
    ) {
        companion object {
            fun Content.toModel(): PinBuddyRequest {
                return PinBuddyRequest(
                    friendRequestStatus = friendRequestStatus,
                    id = id,
                    receiver = receiver.toModel(),
                    sender = sender.toModel()
                )
            }
        }
    }

    companion object {
        fun GetPinBuddyRequestsResponse.toModel(): PagingPinBuddyRequest {
            return PagingPinBuddyRequest(
                totalElements = totalElements,
                totalPages = totalPages,
                pinBuddyRequests = content.map {
                    it.toModel()
                }
            )
        }
    }
}

