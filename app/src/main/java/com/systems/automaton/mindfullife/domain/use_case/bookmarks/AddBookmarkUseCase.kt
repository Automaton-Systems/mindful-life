package com.systems.automaton.mindfullife.domain.use_case.bookmarks

import com.systems.automaton.mindfullife.domain.model.Bookmark
import com.systems.automaton.mindfullife.domain.repository.BookmarkRepository
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(bookmark: Bookmark) = bookmarkRepository.addBookmark(bookmark)
}