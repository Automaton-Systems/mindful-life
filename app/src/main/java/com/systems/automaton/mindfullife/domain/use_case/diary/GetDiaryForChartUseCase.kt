package com.systems.automaton.mindfullife.domain.use_case.diary

import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import com.systems.automaton.mindfullife.domain.repository.DiaryRepository
import com.systems.automaton.mindfullife.util.date.inTheLast30Days
import com.systems.automaton.mindfullife.util.date.inTheLastYear
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDiaryForChartUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(monthly: Boolean) : List<DiaryEntry>{
        return diaryRepository
            .getAllEntries()
            .first()
            .filter {
                if (monthly) it.createdDate.inTheLast30Days()
                else it.createdDate.inTheLastYear()
            }
            .sortedBy { it.createdDate }
    }
}
