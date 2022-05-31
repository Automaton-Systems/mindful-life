package com.systems.automaton.mindfullife.domain.use_case.diary

import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import com.systems.automaton.mindfullife.domain.repository.DiaryRepository
import com.systems.automaton.mindfullife.util.settings.Order
import com.systems.automaton.mindfullife.util.settings.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllEntriesUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    operator fun invoke(order: Order) : Flow<List<DiaryEntry>> {
        return diaryRepository.getAllEntries().map { entries ->
            when (order.orderType) {
                is OrderType.ASC -> {
                    when (order) {
                        is Order.Alphabetical -> entries.sortedBy { it.title }
                        is Order.DateCreated -> entries.sortedBy { it.createdDate }
                        is Order.DateModified -> entries.sortedBy { it.updatedDate }
                        else -> entries.sortedBy { it.updatedDate }
                    }
                }
                is OrderType.DESC -> {
                    when (order) {
                        is Order.Alphabetical -> entries.sortedByDescending { it.title }
                        is Order.DateCreated -> entries.sortedByDescending { it.createdDate }
                        is Order.DateModified -> entries.sortedByDescending { it.updatedDate }
                        else -> entries.sortedByDescending { it.updatedDate }
                    }
                }
            }
        }
    }
}