package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.domain.model.PintsCategory
import com.pinup.placePinup.domain.model.PintsDetail
import com.pinup.placePinup.domain.model.PintsPageAble

interface PintsRepository {
    suspend fun getPints(memberId: Int, pageAble: PageAble): PResult<PintsPageAble>
    suspend fun getPintsDetail(pintsId: Int): PResult<PintsDetail>
    suspend fun deletePints(pintsId: Int): PResult<Unit>
    suspend fun registerPints(request: ModifyPintsRequest): PResult<Int>
    suspend fun editPints(pintsId: Int, request: ModifyPintsRequest): PResult<Int>
    suspend fun getEditorPints(request: GetEditorPintsRequest): PResult<List<PinchListItem>>
    suspend fun getEditorPintsDetail(pintsId: Int): PResult<EditorPintsDetail>
    suspend fun getEditorPintsCategory(): PResult<List<PintsCategory>>
}