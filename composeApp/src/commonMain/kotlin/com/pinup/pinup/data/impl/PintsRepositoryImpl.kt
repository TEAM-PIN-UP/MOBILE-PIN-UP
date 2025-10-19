package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.PintsRemoteDataSource
import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.data.request.pints.PageAble
import com.pinup.pinup.data.response.EditorPintsDetailResponse.Companion.toModel
import com.pinup.pinup.data.response.EditorPintsResponse.Companion.toModel
import com.pinup.pinup.data.response.PintsDetailResponse.Companion.toModel
import com.pinup.pinup.data.response.PintsListResponse.Companion.toModel
import com.pinup.pinup.domain.model.EditorPintsDetail
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.PintsDetail
import com.pinup.pinup.domain.model.PintsPageAble
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.PintsRepository

class PintsRepositoryImpl (
    private val pintsRemoteDataSource: PintsRemoteDataSource,
): PintsRepository {
    override suspend fun getPints(
        memberId: Int,
        pageAble: PageAble
    ): PResult<PintsPageAble> {
        return pintsRemoteDataSource.getPints(memberId, pageAble).map {
            it.toModel()
        }
    }

    override suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetail> {
        return pintsRemoteDataSource.getPintsDetail(pintsId).map {
            it.toModel()
        }
    }

    override suspend fun deletePints(pintsId: Int): PResult<Unit> {
        return pintsRemoteDataSource.deletePints(pintsId)
    }

    override suspend fun registerPints(request: ModifyPintsRequest): PResult<Unit> {
        return pintsRemoteDataSource.registerPints(request)
    }

    override suspend fun editPints(
        pintsId: Int,
        request: ModifyPintsRequest
    ): PResult<Unit> {
        return pintsRemoteDataSource.editPints(pintsId, request)
    }

    override suspend fun getEditorPints(request: GetReviewedPlacesRequest): PResult<List<PinchListItem>> {
        return pintsRemoteDataSource.getEditorPints(request).map {
            it.map { item -> item.toModel() }
        }
    }

    override suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetail> {
        return pintsRemoteDataSource.getEditorPintsDetail(pintsId).map {
            it.toModel()
        }
    }
}