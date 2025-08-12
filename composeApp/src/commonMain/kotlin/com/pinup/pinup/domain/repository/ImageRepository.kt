package com.pinup.pinup.domain.repository

import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult

interface ImageRepository {
    suspend fun uploadSeveralImages(type: ImageUploadType, files: List<ByteArray>): PResult<Unit>
    suspend fun uploadImage(type: ImageUploadType, image: ByteArray): PResult<Unit>
}