package com.bigint.taskmanager.presentation.screens.task_details.state

import com.bigint.taskmanager.domain.model.TaskDto

data class TaskDetailsState(
    val task: TaskDto? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false
)
