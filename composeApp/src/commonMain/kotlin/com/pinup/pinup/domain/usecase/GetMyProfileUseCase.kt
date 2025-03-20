package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetMyProfileUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke() : StateFlow<UserInfo> {
        return membersRepositoryImpl.getUserInfo()
    }
}