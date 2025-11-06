package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.PintsRemoteDataSource
import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.data.response.EditorPintsDetailResponse.Companion.toModel
import com.pinup.placePinup.data.response.EditorPintsResponse.Companion.toModel
import com.pinup.placePinup.data.response.PintsDetailResponse.Companion.toModel
import com.pinup.placePinup.data.response.PintsListResponse.Companion.toModel
import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.domain.model.PintsCategory
import com.pinup.placePinup.domain.model.PintsDetail
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.model.map
import com.pinup.placePinup.domain.repository.PintsRepository

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

    override suspend fun registerPints(request: ModifyPintsRequest): PResult<Int> {
        return pintsRemoteDataSource.registerPints(request).map {
            it.pintsId
        }
    }

    override suspend fun editPints(
        pintsId: Int,
        request: ModifyPintsRequest
    ): PResult<Int> {
        return pintsRemoteDataSource.editPints(pintsId, request).map {
            it.pintsId
        }
    }

    override suspend fun getEditorPints(request: GetEditorPintsRequest): PResult<List<PinchListItem>> {
        return pintsRemoteDataSource.getEditorPints(request).map {
            it.map { item -> item.toModel() }
        }
    }

    override suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetail> {
        return pintsRemoteDataSource.getEditorPintsDetail(pintsId).map {
            it.toModel()
        }
    }

    override suspend fun getEditorPintsCategory(): PResult<List<PintsCategory>> {
        return pintsRemoteDataSource.getEditorPintsCategory().map {
            it.map { category ->
                PintsCategory.of(category)
            }
        }
    }
}