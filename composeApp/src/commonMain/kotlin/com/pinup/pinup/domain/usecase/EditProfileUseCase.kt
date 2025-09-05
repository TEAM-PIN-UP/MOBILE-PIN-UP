package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.MembersRepository

class EditProfileUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(request: ProfileEditRequest): PResult<Unit> {
        return membersRepository.editProfile(request)
    }
}