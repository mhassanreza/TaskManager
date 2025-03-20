package com.bigint.taskmanager.domain.usecase.preferences

import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import javax.inject.Inject

class UpdateCurrentSortOptionUseCase @Inject constructor(
    private val repository: IPreferencesRepository
) {
    suspend operator fun invoke(
        sortOptionValue: Int
    ) {
        return repository.updateCurrentSortOption(sortOptionValue = sortOptionValue)
    }
}