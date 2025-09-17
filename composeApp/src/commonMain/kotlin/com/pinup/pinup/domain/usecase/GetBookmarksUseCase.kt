package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.repository.BookmarksRepository
import com.pinup.pinup.platform.hLog


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