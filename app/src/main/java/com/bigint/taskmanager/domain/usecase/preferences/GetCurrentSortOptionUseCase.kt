package com.bigint.taskmanager.domain.usecase.preferences

import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentSortOptionUseCase @Inject constructor(
    private val repository: IPreferencesRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getCurrentSortOption()
    }
}