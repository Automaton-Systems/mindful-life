package com.systems.automaton.mindfullife.domain.use_case.bookmarks

import com.systems.automaton.mindfullife.domain.repository.BookmarkRepository
import javax.inject.Inject

class GetBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(id: Int) = bookmarkRepository.getBookmark(id)
}