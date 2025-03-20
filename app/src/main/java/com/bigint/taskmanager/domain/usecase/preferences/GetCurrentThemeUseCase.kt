package com.bigint.taskmanager.domain.usecase.preferences

import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentThemeUseCase @Inject constructor(
    private val repository: IPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.getCurrentTheme()
    }
}