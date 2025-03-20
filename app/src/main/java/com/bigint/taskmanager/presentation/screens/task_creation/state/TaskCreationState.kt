package com.bigint.taskmanager.presentation.screens.task_creation.state

import com.bigint.taskmanager.domain.enums.Priority
import java.util.Calendar

data class TaskCreationState(
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long = Calendar.getInstance().timeInMillis, // Default to today
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)