package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.EmailVerifyDataSource
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.EmailApi

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