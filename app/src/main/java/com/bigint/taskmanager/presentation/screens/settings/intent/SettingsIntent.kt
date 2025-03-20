package com.bigint.taskmanager.presentation.screens.settings.intent

import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption

sealed class SettingsIntent {
    data class UpdateSort(val sortBy: SortOption) : SettingsIntent()
    data class UpdateFilter(val filterBy: FilterOption) : SettingsIntent()
    data class UpdateTheme(val isDark: Boolean) : SettingsIntent()   // Update Theme in Preferences
}