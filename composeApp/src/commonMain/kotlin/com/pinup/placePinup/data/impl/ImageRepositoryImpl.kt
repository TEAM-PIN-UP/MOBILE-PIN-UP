package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.ImageRemoteDataSource
import com.pinup.placePinup.domain.model.ImageUploadType
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ImageRepository

class ImageRepositoryImpl (
    private val imageRemoteDataSource: ImageRemoteDataSource,
) : ImageRepository {

    override suspend fun uploadSeveralImages(
        type: ImageUploadType,
        files: List<ByteArray>
    ): PResult<List<String>> {
        return imageRemoteDataSource.uploadSeveralImages(type, files)
    }

    override suspend fun uploadImage(
        type: ImageUploadType,
        image: ByteArray
    ): PResult<String> {
        return imageRemoteDataSource.uploadImage(type, image)
    }
}