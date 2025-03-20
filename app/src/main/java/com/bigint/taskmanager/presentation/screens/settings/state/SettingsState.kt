package com.bigint.taskmanager.presentation.screens.settings.state

import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.model.TaskDto

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val sortBy: SortOption = SortOption.PRIORITY,
    val filterBy: FilterOption = FilterOption.ALL
)