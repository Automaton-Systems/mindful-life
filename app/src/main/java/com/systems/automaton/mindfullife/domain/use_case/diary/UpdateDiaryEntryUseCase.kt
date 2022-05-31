package com.systems.automaton.mindfullife.domain.use_case.diary

import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import com.systems.automaton.mindfullife.domain.repository.DiaryRepository
import javax.inject.Inject

class UpdateDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(entry: DiaryEntry) = diaryRepository.updateEntry(entry)
}