package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ImageRepository

class PostImageUploadUseCase (
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(type: ImageUploadType, image: ByteArray): PResult<Unit> {
        return imageRepository.uploadImage(type, image)
    }
}