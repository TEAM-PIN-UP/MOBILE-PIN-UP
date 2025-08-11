package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface EmailApi {
    @POST(ApiPath.MailPath.VERIFY_CODE)
    suspend fun verifyEmail(@Body request: EmailVerifyRequest): PResult<PResponse<Unit>>

    @POST(ApiPath.MailPath.SEND_CODE)
    suspend fun sendVerifyCode(@Body request: SendVerifyCodeRequest): PResult<PResponse<Unit>>
}