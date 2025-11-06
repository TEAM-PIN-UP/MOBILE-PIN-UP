package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.ImageUploadType
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ImageRepository

class PostSeveralImagesUploadUseCase (
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(type: ImageUploadType, files: List<ByteArray>): PResult<List<String>> {
        return imageRepository.uploadSeveralImages(type, files)
    }
}