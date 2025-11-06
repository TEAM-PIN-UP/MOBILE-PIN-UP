package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.BookmarksRepository


class AddBookmarkUseCase (
    private val bookmarksRepository: BookmarksRepository
) {
    suspend operator fun invoke(kakaoPlaceId: String): PResult<Int> {
       return bookmarksRepository.addBookmark(kakaoPlaceId)
    }
}