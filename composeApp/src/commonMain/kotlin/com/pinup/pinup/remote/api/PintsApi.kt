package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.data.request.pints.PageAble
import com.pinup.pinup.data.response.EditorPintsDetailResponse
import com.pinup.pinup.data.response.EditorPintsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.PintsDetailResponse
import com.pinup.pinup.data.response.PintsListResponse
import com.pinup.pinup.data.response.UserPintsEditResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PintsCategory
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface PintsApi {

    companion object {
        const val PATH_PINTS_ID = "pintsId"
        const val QUERY_MEMBER_ID = "memberId"
        const val QUERY_PAGEABLE = "pageable"
    }

    @POST(ApiPath.Pints.PINTS)
    suspend fun registerPints(
        @Body request: ModifyPintsRequest
    ): PResult<PResponse<UserPintsEditResponse>>

    @GET(ApiPath.Pints.PINTS)
    suspend fun getPints(
        @Query(QUERY_MEMBER_ID) memberId: Int,
        @Query(QUERY_PAGEABLE) pageable: PageAble,
    ): PResult<PResponse<PintsListResponse>>

    @DELETE(ApiPath.Pints.PINTS_MODIFY)
    suspend fun deletePints(
        @Path(PATH_PINTS_ID) pintsId: Int,
    ): PResult<PResponse<Unit>>

    @PUT(ApiPath.Pints.PINTS_MODIFY)
    suspend fun editPints(
        @Path(PATH_PINTS_ID) pintsId: Int,
        @Body request: ModifyPintsRequest
    ): PResult<PResponse<UserPintsEditResponse>>

    @GET(ApiPath.Pints.PINTS_MODIFY)
    suspend fun getPintsDetail(
        @Path(PATH_PINTS_ID) pintsId: Int,
    ): PResult<PResponse<PintsDetailResponse>>

    @GET(ApiPath.Pints.EDITOR)
    suspend fun getEditorPints(
        @Query("category") category: PintsCategory,
        @Query("swLatitude") swLatitude: String,
        @Query("swLongitude") swLongitude: String,
        @Query("neLatitude") neLatitude: String,
        @Query("neLongitude") neLongitude: String,
    ): PResult<PResponse<List<EditorPintsResponse>>>

    @GET(ApiPath.Pints.EDITOR_PINCH)
    suspend fun getEditorPintsDetail(
        @Path(PATH_PINTS_ID) pintsId: Int,
    ): PResult<PResponse<EditorPintsDetailResponse>>

    @GET(ApiPath.Pints.EDITOR_CATEGORY)
    suspend fun getEditorPintsCategory(): PResult<PResponse<List<String>>>
}