package com.bigint.taskmanager.domain.usecase.preferences

import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import javax.inject.Inject

class UpdateFilterOptionUseCase @Inject constructor(
    private val repository: IPreferencesRepository
) {
    suspend operator fun invoke(
        filterOptionValue: Int
    ) {
        return repository.updateFilterOption(filterOptionValue = filterOptionValue)
    }
}