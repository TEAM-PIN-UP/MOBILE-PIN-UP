package com.pinup.pinup.data.remote

import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult

interface ImageRemoteDataSource {
    suspend fun uploadSeveralImages(type: ImageUploadType, files: List<ByteArray>): PResult<Unit>
    suspend fun uploadImage(type: ImageUploadType, image: ByteArray): PResult<Unit>
}