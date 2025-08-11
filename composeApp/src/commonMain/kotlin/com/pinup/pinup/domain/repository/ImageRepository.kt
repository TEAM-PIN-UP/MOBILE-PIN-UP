package com.pinup.pinup.domain.repository

import com.pinup.pinup.domain.model.PResult

interface ImageRepository {
    suspend fun uploadSeveralImages(type: String, files: List<ByteArray>): PResult<Unit>
    suspend fun uploadImage(type: String, image: ByteArray): PResult<Unit>
}