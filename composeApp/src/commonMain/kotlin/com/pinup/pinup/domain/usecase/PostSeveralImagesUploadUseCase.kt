package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ImageRepository

class PostSeveralImagesUploadUseCase (
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(type: String, files: List<ByteArray>): PResult<Unit> {
        return imageRepository.uploadSeveralImages(type, files)
    }
}