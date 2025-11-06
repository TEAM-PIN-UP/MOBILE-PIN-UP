package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.EmailVerifyDataSource
import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.EmailVerifyRepository

class EmailVerifyRepositoryImpl (
    private val emailVerifyDataSource: EmailVerifyDataSource,
) : EmailVerifyRepository {
    override suspend fun verifyEmail(request: EmailVerifyRequest): PResult<Unit> {
        return emailVerifyDataSource.verifyEmail(request)
    }

    override suspend fun sendVerifyCode(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailVerifyDataSource.sendVerifyCode(request)
    }

    override suspend fun sendTemporaryPassword(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailVerifyDataSource.sendTemporaryPassword(request)
    }
}