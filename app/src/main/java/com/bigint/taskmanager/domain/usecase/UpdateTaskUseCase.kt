package com.bigint.taskmanager.domain.usecase

import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.repository.ITaskRepository

/**
 * Use case to update an existing task in the repository.
 */
class UpdateTaskUseCase(private val repository: ITaskRepository) {
    /**
     * Invokes the use case to update a task.
     * @param task The task to update with new values.
     */
    suspend operator fun invoke(task: TaskDto) {
        repository.updateTask(task)
    }
}