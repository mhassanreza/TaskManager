package com.bigint.taskmanager.domain.usecase.preferences

data class PreferencesUseCases(
    val updateCurrentThemeUseCase: UpdateCurrentThemeUseCase,
    val getCurrentThemeUseCase: GetCurrentThemeUseCase,
    val updateFilterOptionUseCase: UpdateFilterOptionUseCase,
    val getCurrentFilterOptionUseCase: GetCurrentFilterOptionUseCase,
    val updateCurrentSortOptionUseCase: UpdateCurrentSortOptionUseCase,
    val getCurrentSortOptionUseCase: GetCurrentSortOptionUseCase
)
