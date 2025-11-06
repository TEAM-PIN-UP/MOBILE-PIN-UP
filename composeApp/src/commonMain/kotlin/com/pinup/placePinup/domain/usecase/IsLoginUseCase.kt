package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.repository.MembersRepository
import com.pinup.placePinup.ui.login.model.SNSType

class IsLoginUseCase(
    private val membersRepository: MembersRepository,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase
) {
    suspend operator fun invoke(): Pair<Boolean, Boolean> {
        val isLogin = membersRepository.getIsUsedApp()
        val result = getMemberInfoUseCase().getSuccessOrNull()
        if (result != null) {
            saveUserInfoUseCase(
                UserInfo(
                    memberId = result.profile.memberId,
                    email = result.profile.email,
                    name = result.profile.name,
                    nickname = result.profile.nickname,
                    profileUrl = result.profile.profilePictureUrl ?: "",
                    snsType = SNSType.GOOGLE
                )
            )
        }
        return Pair(isLogin, result != null)
    }
}