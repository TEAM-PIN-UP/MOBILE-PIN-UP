package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.EmailVerifyRepository

class PostEmailVerifyUseCase (
    private val emailVerifyRepository: EmailVerifyRepository
) {
    suspend operator fun invoke(request: EmailVerifyRequest): PResult<Unit> {
        return emailVerifyRepository.verifyEmail(request)
    }
}