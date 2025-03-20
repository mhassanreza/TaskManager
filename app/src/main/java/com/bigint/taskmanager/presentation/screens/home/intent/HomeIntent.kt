package com.bigint.taskmanager.presentation.screens.home.intent

import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.model.TaskDto

sealed class HomeIntent {
    data object LoadTasks : HomeIntent()
    data object NavigateToTaskCreation : HomeIntent()
    data class NavigateToTaskDetails(val taskId: Int) : HomeIntent()
    data class UpdateSort(val sortBy: SortOption) : HomeIntent()
    data class UpdateFilter(val filterBy: FilterOption) : HomeIntent()
    data class DeleteTask(val task: TaskDto) : HomeIntent() // Added for swipe-to-delete
    data class AddTask(val task: TaskDto) : HomeIntent()   // Added for undo
    data class UpdateTheme(val isDark: Boolean) : HomeIntent()   // Update Theme is Preferences
    data class ReorderTask(val fromIndex: Int, val toIndex: Int) : HomeIntent()
}