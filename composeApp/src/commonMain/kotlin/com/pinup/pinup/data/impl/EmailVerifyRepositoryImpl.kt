package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.BookmarksRemoteDataSource
import com.pinup.pinup.data.remote.EmailVerifyDataSource
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.BookmarksRepository
import com.pinup.pinup.domain.repository.EmailVerifyRepository

class EmailVerifyRepositoryImpl (
    private val emailVerifyDataSource: EmailVerifyDataSource,
) : EmailVerifyRepository {
    override suspend fun verifyEmail(request: EmailVerifyRequest): PResult<Unit> {
        return emailVerifyDataSource.verifyEmail(request)
    }

    override suspend fun sendVerifyCode(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailVerifyDataSource.sendVerifyCode(request)
    }
}