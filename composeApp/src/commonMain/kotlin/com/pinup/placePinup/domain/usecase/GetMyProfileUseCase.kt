package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow


class GetMyProfileUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke() : StateFlow<UserInfo> {
        return membersRepositoryImpl.getUserInfo()
    }
}