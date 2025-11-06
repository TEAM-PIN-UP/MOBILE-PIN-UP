package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.domain.model.PResult

interface EmailVerifyRepository {
    suspend fun verifyEmail(request : EmailVerifyRequest): PResult<Unit>
    suspend fun sendVerifyCode(request : SendVerifyCodeRequest): PResult<Unit>
    suspend fun sendTemporaryPassword(request: SendVerifyCodeRequest): PResult<Unit>
}