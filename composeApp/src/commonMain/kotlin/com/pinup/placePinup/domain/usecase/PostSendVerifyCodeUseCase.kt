package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.EmailVerifyRepository

class PostSendVerifyCodeUseCase (
    private val emailVerifyRepository: EmailVerifyRepository
) {
    suspend operator fun invoke(request: SendVerifyCodeRequest): PResult<Unit> {
        return emailVerifyRepository.sendVerifyCode(request)
    }
}