package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.PintsRemoteDataSource
import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.data.response.EditorPintsDetailResponse
import com.pinup.placePinup.data.response.EditorPintsResponse
import com.pinup.placePinup.data.response.PintsDetailResponse
import com.pinup.placePinup.data.response.PintsListResponse
import com.pinup.placePinup.data.response.UserPintsEditResponse
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.PintsApi

class PintsRemoteDataSourceImpl (
    private val pintsApi: PintsApi
) : PintsRemoteDataSource {

    override suspend fun getPints(
        memberId: Int,
        pageAble: PageAble
    ): PResult<PintsListResponse> {
        return pintsApi.getPints(memberId, pageAble).mapSuccessData()
    }

    override suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetailResponse> {
        return pintsApi.getPintsDetail(pintsId).mapSuccessData()
    }

    override suspend fun deletePints(pintsId: Int): PResult<Unit> {
        return pintsApi.deletePints(pintsId).mapSuccessData()
    }

    override suspend fun registerPints(request: ModifyPintsRequest): PResult<UserPintsEditResponse> {
        return pintsApi.registerPints(request).mapSuccessData()
    }

    override suspend fun editPints(pintsId: Int, request: ModifyPintsRequest): PResult<UserPintsEditResponse> {
        return pintsApi.editPints(pintsId, request).mapSuccessData()
    }

    override suspend fun getEditorPints(request: GetEditorPintsRequest): PResult<List<EditorPintsResponse>> {
        return pintsApi.getEditorPints(
            category = request.category,
            swLatitude = request.swLatitude,
            swLongitude = request.swLongitude,
            neLatitude = request.neLatitude,
            neLongitude = request.neLongitude,
        ).mapSuccessData()
    }

    override suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetailResponse> {
        return pintsApi.getEditorPintsDetail(pintsId).mapSuccessData()
    }

    override suspend fun getEditorPintsCategory(): PResult<List<String>> {
        return pintsApi.getEditorPintsCategory().mapSuccessData()
    }
}