package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.repository.MembersRepository


class SaveUserInfoUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(userInfo: UserInfo) {
        return membersRepository.saveUserInfo(userInfo)
    }
}