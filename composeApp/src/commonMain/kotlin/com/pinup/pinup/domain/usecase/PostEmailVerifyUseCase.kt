package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.EmailVerifyRepository

class PostEmailVerifyUseCase (
    private val emailVerifyRepository: EmailVerifyRepository
) {
    suspend operator fun invoke(request: EmailVerifyRequest): PResult<Unit> {
        return emailVerifyRepository.verifyEmail(request)
    }
}