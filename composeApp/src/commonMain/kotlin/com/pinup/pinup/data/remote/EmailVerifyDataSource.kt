package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.PResult

interface EmailVerifyDataSource {
    suspend fun verifyEmail(request: EmailVerifyRequest): PResult<Unit>
    suspend fun sendVerifyCode(request: SendVerifyCodeRequest): PResult<Unit>
}