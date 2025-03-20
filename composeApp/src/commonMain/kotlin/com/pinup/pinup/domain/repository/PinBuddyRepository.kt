package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingPinBuddy
import com.pinup.pinup.domain.model.PagingPinBuddyRequest
import com.pinup.pinup.domain.model.PinBuddyRequest
import com.pinup.pinup.domain.model.Profile

interface PinBuddyRepository {
    suspend fun getPinBuddies(page: Int, size: Int): PResult<PagingPinBuddy>
    suspend fun getSentPinBuddyRequests(page: Int, size: Int): PResult<PagingPinBuddyRequest>
    suspend fun getReceivedPinBuddyRequests(page: Int, size: Int): PResult<PagingPinBuddyRequest>
    suspend fun deletePinBuddy(friendId: String): PResult<Unit>
    suspend fun requestPinBuddy(receiverId: Int): PResult<Unit>
    suspend fun deleteRequestPinBuddy(friendRequestId: Int): PResult<Unit>
    suspend fun rejectPinBuddy(friendRequestId: Int): PResult<Unit>
    suspend fun acceptPinBuddy(friendRequestId: Int): PResult<Unit>
}