package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.repository.BookmarksRepository


class GetBookmarksUseCase (
    private val bookmarksRepository: BookmarksRepository
) {
    suspend operator fun invoke(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String
    ): PResult<List<BookmarkedPlace>> {
       return bookmarksRepository.getBookmarks(
           category = category,
           sort = sort,
           currentLatitude = currentLatitude,
           currentLongitude = currentLongitude,
       )
    }
}