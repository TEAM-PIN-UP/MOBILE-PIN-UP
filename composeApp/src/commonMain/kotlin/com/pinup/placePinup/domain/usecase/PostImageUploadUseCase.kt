package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.ImageUploadType
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ImageRepository

class PostImageUploadUseCase (
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(type: ImageUploadType, image: ByteArray): PResult<String> {
        return imageRepository.uploadImage(type, image)
    }
}