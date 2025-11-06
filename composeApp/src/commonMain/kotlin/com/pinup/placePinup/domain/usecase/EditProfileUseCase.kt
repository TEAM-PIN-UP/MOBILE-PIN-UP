package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.ProfileEditRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.MembersRepository

class EditProfileUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(request: ProfileEditRequest): PResult<Unit> {
        return membersRepository.editProfile(request)
    }
}