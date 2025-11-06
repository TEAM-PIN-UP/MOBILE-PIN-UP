package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.ChangePasswordRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.MembersRepository

class ChangePasswordUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(request: ChangePasswordRequest): PResult<Unit> {
        return membersRepository.changePassword(request)
    }
}