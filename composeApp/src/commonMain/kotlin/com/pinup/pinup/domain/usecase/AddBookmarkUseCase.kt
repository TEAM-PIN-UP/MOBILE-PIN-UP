package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.BookmarksRepository
import javax.inject.Inject

class AddBookmarkUseCase (
    private val bookmarksRepository: BookmarksRepository
) {
    suspend operator fun invoke(kakaoPlaceId: String): PResult<Int> {
       return bookmarksRepository.addBookmark(kakaoPlaceId)
    }
}