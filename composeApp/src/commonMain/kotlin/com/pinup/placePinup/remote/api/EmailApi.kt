package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface EmailApi {
    @POST(ApiPath.MailPath.VERIFY_CODE)
    suspend fun verifyEmail(@Body request: EmailVerifyRequest): PResult<PResponse<Unit>>

    @POST(ApiPath.MailPath.SEND_CODE)
    suspend fun sendVerifyCode(@Body request: SendVerifyCodeRequest): PResult<PResponse<Unit>>
    @POST(ApiPath.MailPath.SEND_PASSWORD)
    suspend fun sendTemporaryPassword(@Body request: SendVerifyCodeRequest): PResult<PResponse<Unit>>
}