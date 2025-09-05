package com.pinup.pinup.ui.profilesetting

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class ProfileSettingViewModel(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
) : BaseViewModel<ProfileSettingUiState, ProfileSettingUiEvent>(ProfileSettingUiState()) {

    companion object {
        const val MAX_NICKNAME = 12
        const val MAX_BIO = 60
    }

    init {
        getMyProfile()
    }

    private fun getMyProfile() = viewModelScope.launch {
        resultResponse(
            response = getMemberInfoUseCase(),
            successCallback = {
                updateState {
                    copy(
                        profileUrl = it.profile.profilePictureUrl,
                        nickName = it.profile.nickname,
                        bio = it.profile.bio
                    )
                }
            }
        )
    }

    fun updateProfile(profileByte: ByteArray) {
        updateState { copy( profileByte = profileByte ) }
    }

    fun updateNickName(nickName: String) {
        if(nickName.length > MAX_NICKNAME) return
        updateState {
            copy(
                nickName = nickName
            )
        }
    }

    fun updateBio(bio: String) {
        if(bio.length > MAX_BIO) return
        updateState {
            copy(
                bio = bio
            )
        }
    }

    fun modifyProfile() {

    }

}

data class ProfileSettingUiState(
    val profileUrl: String? = null,
    val profileByte: ByteArray? = null,
    val nickName: String = "",
    val bio: String = "",
): UiState

sealed interface ProfileSettingUiEvent : UiEvent {
    data object ErrorCantChangeName : ProfileSettingUiEvent
    data object ErrorDuplicateName : ProfileSettingUiEvent
    data object SuccessChangeProfile : ProfileSettingUiEvent
}

