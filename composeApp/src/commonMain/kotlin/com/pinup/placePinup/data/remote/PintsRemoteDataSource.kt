package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.data.response.EditorPintsDetailResponse
import com.pinup.placePinup.data.response.EditorPintsResponse
import com.pinup.placePinup.data.response.PintsDetailResponse
import com.pinup.placePinup.data.response.PintsListResponse
import com.pinup.placePinup.data.response.UserPintsEditResponse
import com.pinup.placePinup.domain.model.PResult

interface PintsRemoteDataSource {
    suspend fun getPints(memberId: Int, pageAble: PageAble): PResult<PintsListResponse>
    suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetailResponse>
    suspend fun deletePints(pintsId: Int): PResult<Unit>
    suspend fun registerPints(request: ModifyPintsRequest): PResult<UserPintsEditResponse>
    suspend fun editPints(pintsId: Int, request: ModifyPintsRequest): PResult<UserPintsEditResponse>
    suspend fun getEditorPints(request: GetEditorPintsRequest): PResult<List<EditorPintsResponse>>
    suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetailResponse>
    suspend fun getEditorPintsCategory(): PResult<List<String>>
}