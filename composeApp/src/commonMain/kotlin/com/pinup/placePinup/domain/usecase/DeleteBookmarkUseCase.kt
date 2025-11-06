package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.BookmarksRepository


class DeleteBookmarkUseCase (
    private val bookmarksRepository: BookmarksRepository
) {
    suspend operator fun invoke(kakaoPlaceId: String): PResult<Unit> {
       return bookmarksRepository.deleteBookmark(kakaoPlaceId)
    }
}