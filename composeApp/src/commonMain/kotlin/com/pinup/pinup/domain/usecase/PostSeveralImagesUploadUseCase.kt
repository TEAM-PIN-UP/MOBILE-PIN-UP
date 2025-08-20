package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ImageRepository

class PostSeveralImagesUploadUseCase (
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(type: ImageUploadType, files: List<ByteArray>): PResult<List<String>> {
        return imageRepository.uploadSeveralImages(type, files)
    }
}