package com.bigint.taskmanager.presentation.screens.home.state

import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.model.TaskDto

data class HomeState(
    val tasks: List<TaskDto> = emptyList(),
    val isLoading: Boolean = false,

    val isDarkTheme: Boolean = false,
    val sortBy: SortOption = SortOption.PRIORITY,
    val filterBy: FilterOption = FilterOption.ALL
)