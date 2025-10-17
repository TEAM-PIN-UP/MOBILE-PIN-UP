package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.data.request.pints.PageAble
import com.pinup.pinup.data.response.PintsDetailResponse
import com.pinup.pinup.data.response.PintsListResponse
import com.pinup.pinup.domain.model.PResult

interface PintsRemoteDataSource {
    suspend fun getPints(memberId: Int, pageAble: PageAble): PResult<PintsListResponse>
    suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetailResponse>
    suspend fun deletePints(pintsId: Int): PResult<Unit>
    suspend fun registerPints(request: ModifyPintsRequest): PResult<Unit>
    suspend fun editPints(pintsId: Int, request: ModifyPintsRequest): PResult<Unit>
}