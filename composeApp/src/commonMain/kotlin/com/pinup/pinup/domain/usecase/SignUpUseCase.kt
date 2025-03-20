package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase (
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(signUpInfo: SignUpInfo): PResult<Unit> {
       return authRepository.signUp(signUpInfo)
    }
}