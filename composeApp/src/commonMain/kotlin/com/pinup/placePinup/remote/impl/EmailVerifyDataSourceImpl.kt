package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.EmailVerifyDataSource
import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.EmailApi

class EmailVerifyDataSourceImpl(
    private val emailApi: EmailApi
) : EmailVerifyDataSource {
    override suspend fun verifyEmail(request: EmailVerifyRequest): PResult<Unit> {
        return emailApi.verifyEmail(request).mapSuccessData()
    }

    override suspend fun sendVerifyCode(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailApi.sendVerifyCode(request).mapSuccessData()
    }

    override suspend fun sendTemporaryPassword(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailApi.sendTemporaryPassword(request).mapSuccessData()
    }
}