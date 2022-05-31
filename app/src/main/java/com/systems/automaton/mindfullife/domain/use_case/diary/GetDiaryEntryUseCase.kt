package com.systems.automaton.mindfullife.domain.use_case.diary

import com.systems.automaton.mindfullife.domain.repository.DiaryRepository
import javax.inject.Inject

class GetDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(id: Int) = diaryRepository.getEntry(id)
}