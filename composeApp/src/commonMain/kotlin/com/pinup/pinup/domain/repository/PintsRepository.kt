package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.data.request.pints.PageAble
import com.pinup.pinup.domain.model.EditorPintsDetail
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.PintsCategory
import com.pinup.pinup.domain.model.PintsDetail
import com.pinup.pinup.domain.model.PintsPageAble

interface PintsRepository {
    suspend fun getPints(memberId: Int, pageAble: PageAble): PResult<PintsPageAble>
    suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetail>
    suspend fun deletePints(pintsId: Int): PResult<Unit>
    suspend fun registerPints(request: ModifyPintsRequest): PResult<Unit>
    suspend fun editPints(pintsId: Int, request: ModifyPintsRequest): PResult<Unit>
    suspend fun getEditorPints(request: GetReviewedPlacesRequest): PResult<List<PinchListItem>>
    suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetail>
    suspend fun getEditorPintsCategory(): PResult<List<PintsCategory>>
}