package com.systems.automaton.mindfullife.domain.use_case.diary

import com.systems.automaton.mindfullife.domain.repository.DiaryRepository
import javax.inject.Inject

class SearchEntriesUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(query: String) = repository.searchEntries(query)
}