package com.systems.automaton.mindfullife.domain.use_case.bookmarks

import com.systems.automaton.mindfullife.domain.repository.BookmarkRepository
import javax.inject.Inject

class SearchBookmarksUseCase @Inject constructor(
    private val bookmarksRepository: BookmarkRepository
) {
    suspend operator fun invoke(query: String) = bookmarksRepository.searchBookmarks(query)
}