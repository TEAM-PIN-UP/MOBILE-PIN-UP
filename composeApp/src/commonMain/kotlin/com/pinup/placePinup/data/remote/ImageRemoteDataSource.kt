package com.pinup.placePinup.data.remote

import com.pinup.placePinup.domain.model.ImageUploadType
import com.pinup.placePinup.domain.model.PResult

interface ImageRemoteDataSource {
    suspend fun uploadSeveralImages(type: ImageUploadType, files: List<ByteArray>): PResult<List<String>>
    suspend fun uploadImage(type: ImageUploadType, image: ByteArray): PResult<String>
}