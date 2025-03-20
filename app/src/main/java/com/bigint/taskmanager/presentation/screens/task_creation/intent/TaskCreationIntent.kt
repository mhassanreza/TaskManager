package com.bigint.taskmanager.presentation.screens.task_creation.intent

import com.bigint.taskmanager.domain.enums.Priority

sealed class TaskCreationIntent {
    data class UpdateTitle(val title: String) : TaskCreationIntent()
    data class UpdateDescription(val description: String) : TaskCreationIntent()
    data class UpdatePriority(val priority: Priority) : TaskCreationIntent()
    data class UpdateDueDate(val dueDate: Long) : TaskCreationIntent()
    data object SaveTask : TaskCreationIntent()
}