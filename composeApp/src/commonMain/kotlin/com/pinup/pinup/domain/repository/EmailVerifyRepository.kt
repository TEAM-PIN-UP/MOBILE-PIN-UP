package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.PResult

interface EmailVerifyRepository {
    suspend fun verifyEmail(request : EmailVerifyRequest): PResult<Unit>
    suspend fun sendVerifyCode(request : SendVerifyCodeRequest): PResult<Unit>
}