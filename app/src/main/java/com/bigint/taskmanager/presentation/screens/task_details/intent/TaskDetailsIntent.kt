package com.bigint.taskmanager.presentation.screens.task_details.intent

import com.bigint.taskmanager.domain.model.TaskDto

sealed class TaskDetailsIntent {
    data class LoadTask(val taskId: Int) : TaskDetailsIntent()
    data class MarkComplete(val task: TaskDto) : TaskDetailsIntent()
    data class DeleteTask(val task: TaskDto) : TaskDetailsIntent()
}