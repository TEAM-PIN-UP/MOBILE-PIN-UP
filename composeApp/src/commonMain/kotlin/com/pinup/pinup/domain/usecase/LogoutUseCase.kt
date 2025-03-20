package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.domain.repository.MembersRepository
import javax.inject.Inject

class LogoutUseCase (
    private val authRepository: AuthRepository,
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke() {
        authRepository.logout()
        membersRepository.clearUserData()
    }
}