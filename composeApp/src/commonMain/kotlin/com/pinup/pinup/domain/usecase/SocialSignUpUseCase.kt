package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.repository.AuthRepository


class SocialSignUpUseCase (
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(signUpInfo: SignUpInfo): PResult<Unit> {
       return authRepository.socialSignUp(signUpInfo)
    }
}