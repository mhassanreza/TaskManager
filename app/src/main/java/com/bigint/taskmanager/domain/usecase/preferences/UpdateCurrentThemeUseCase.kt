package com.bigint.taskmanager.domain.usecase.preferences

import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import javax.inject.Inject

class UpdateCurrentThemeUseCase @Inject constructor(
    private val repository: IPreferencesRepository
) {
    suspend operator fun invoke(
        isDarkTheme: Boolean
    ) {
        return repository.updateCurrentTheme(isDarkTheme = isDarkTheme)
    }
}