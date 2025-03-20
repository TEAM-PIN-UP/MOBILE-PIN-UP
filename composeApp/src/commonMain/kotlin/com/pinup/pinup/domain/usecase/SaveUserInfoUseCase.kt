package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.MembersRepository
import javax.inject.Inject

class SaveUserInfoUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(userInfo: UserInfo) {
        return membersRepository.saveUserInfo(userInfo)
    }
}