package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.PinBuddyRemoteDataSource
import com.pinup.placePinup.data.request.RequestPinBuddyRequest
import com.pinup.placePinup.data.response.GetPinBuddiesResponse.Companion.toModel
import com.pinup.placePinup.data.response.GetPinBuddyRequestsResponse.Companion.toModel
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingPinBuddy
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.domain.model.map
import com.pinup.placePinup.domain.repository.PinBuddyRepository

class PinBuddyRepositoryImpl(
    private val pinBuddyRemoteDataSource: PinBuddyRemoteDataSource,
) : PinBuddyRepository {
    override suspend fun getPinBuddies(page: Int, size: Int): PResult<PagingPinBuddy> {
        return pinBuddyRemoteDataSource.getPinBuddies(page, size)
            .map { response ->
                response.toModel()
            }
    }

    override suspend fun getSentPinBuddyRequests(
        page: Int,
        size: Int
    ): PResult<PagingPinBuddyRequest> {
        return pinBuddyRemoteDataSource.getSentPinBuddyRequests(page, size)
            .map { response ->
                response.toModel()
            }
    }

    override suspend fun getReceivedPinBuddyRequests(
        page: Int,
        size: Int
    ): PResult<PagingPinBuddyRequest> {
        return pinBuddyRemoteDataSource.getReceivedPinBuddyRequests(page, size)
            .map { response ->
                response.toModel()
            }
    }

    override suspend fun deletePinBuddy(friendId: String): PResult<Unit> {
        return pinBuddyRemoteDataSource.deletePinBuddy(friendId)
    }

    override suspend fun requestPinBuddy(receiverId: Int): PResult<Unit> {
        return pinBuddyRemoteDataSource.requestPinBuddy(RequestPinBuddyRequest(receiverId))
    }

    override suspend fun deleteRequestPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyRemoteDataSource.deleteRequestPinBuddy(friendRequestId)
    }

    override suspend fun rejectPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyRemoteDataSource.rejectPinBuddy(friendRequestId)
    }

    override suspend fun acceptPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyRemoteDataSource.acceptPinBuddy(friendRequestId)
    }
}