package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.EmailVerifyRepository

class PostTemporaryPasswordUseCase(
    private val emailVerifyRepository: EmailVerifyRepository,
) {
    suspend operator fun invoke(
        request: SendVerifyCodeRequest,
    ): PResult<Unit> {
        return emailVerifyRepository.sendTemporaryPassword(request)
    }
}