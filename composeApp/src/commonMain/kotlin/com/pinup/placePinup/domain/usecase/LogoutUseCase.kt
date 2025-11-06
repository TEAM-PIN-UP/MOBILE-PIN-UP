package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.AuthRepository
import com.pinup.placePinup.domain.repository.MembersRepository


class LogoutUseCase (
    private val authRepository: AuthRepository,
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke() {
        authRepository.logout()
        membersRepository.clearUserData()
    }
}