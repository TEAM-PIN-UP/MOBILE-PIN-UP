package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.ImageRemoteDataSource
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ImageRepository

class ImageRepositoryImpl (
    private val imageRemoteDataSource: ImageRemoteDataSource,
) : ImageRepository {

    override suspend fun uploadSeveralImages(
        type: ImageUploadType,
        files: List<ByteArray>
    ): PResult<Unit> {
        return imageRemoteDataSource.uploadSeveralImages(type, files)
    }

    override suspend fun uploadImage(
        type: ImageUploadType,
        image: ByteArray
    ): PResult<Unit> {
        return imageRemoteDataSource.uploadImage(type, image)
    }
}