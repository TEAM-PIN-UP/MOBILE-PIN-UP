package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.data.response.NotificationItemResponse
import com.pinup.placePinup.data.response.NotificationResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface CallApi {

    companion object{
        const val QUERY_TOKEN = "token"
        const val PATH_NOTIFICATION_ID = "notificationId"
    }

    @POST(ApiPath.Call.DEVICE_TOKEN)
    suspend fun setDeviceToken(@Body request: SetDeviceTokenRequest): PResult<PResponse<Unit>>

    @DELETE(ApiPath.Call.DEVICE_TOKEN)
    suspend fun deleteDeviceToken(
        @Query(QUERY_TOKEN) token: String
    ): PResult<PResponse<Unit>>

    @PATCH(ApiPath.Call.READ_NOTIFICATION)
    suspend fun readNotification(
        @Path(PATH_NOTIFICATION_ID) path: Int
    ): PResult<PResponse<Unit>>

    @PATCH(ApiPath.Call.READ_ALL_NOTIFICATION)
    suspend fun readAllNotification(): PResult<PResponse<Unit>>

    @GET(ApiPath.Call.NOTIFICATION)
    suspend fun getMyNotification(
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): PResult<PResponse<NotificationResponse>>

    @PATCH(ApiPath.Call.DELETE_NOTIFICATION)
    suspend fun deleteNotification(
        @Path(PATH_NOTIFICATION_ID) path: Int
    ): PResult<PResponse<Unit>>

}