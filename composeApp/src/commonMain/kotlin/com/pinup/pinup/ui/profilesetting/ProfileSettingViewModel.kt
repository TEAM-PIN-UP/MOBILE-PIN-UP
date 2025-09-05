package com.pinup.pinup.ui.profilesetting

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.usecase.EditProfileUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.PostImageUploadUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class ProfileSettingViewModel(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val editProfileUseCase: EditProfileUseCase,
    private val uploadImageUseCase: PostImageUploadUseCase
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
                        profileUrl = it.profile.profilePictureUrl ?: "",
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

    fun onClickEditButton() {
        if (uiState.value.profileByte != null) {
            uploadProfileImage()
        } else {
            modifyProfile(uiState.value.profileUrl)
        }
    }

    private fun uploadProfileImage() = viewModelScope.launch {
        resultResponse(
            response = uploadImageUseCase(ImageUploadType.PROFILES, uiState.value.profileByte!!),
            successCallback = {
                modifyProfile(it)
            }
        )
    }

    private fun modifyProfile(profile: String) = viewModelScope.launch {
        val request = ProfileEditRequest(
            nickname = uiState.value.nickName,
            bio = uiState.value.bio,
            profileImageUrl = profile
        )

        resultResponse(
            response = editProfileUseCase(request),
            successCallback = {
                emitEvent(ProfileSettingUiEvent.SuccessChangeProfile)
            }
        )
    }
}

data class ProfileSettingUiState(
    val profileUrl: String = "",
    val profileByte: ByteArray? = null,
    val nickName: String = "",
    val bio: String = "",
): UiState

sealed interface ProfileSettingUiEvent : UiEvent {
    data object ErrorCantChangeName : ProfileSettingUiEvent
    data object ErrorDuplicateName : ProfileSettingUiEvent
    data object SuccessChangeProfile : ProfileSettingUiEvent
}

