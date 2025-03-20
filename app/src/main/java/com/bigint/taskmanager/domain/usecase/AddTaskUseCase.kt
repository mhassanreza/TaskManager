package com.bigint.taskmanager.domain.usecase

import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.repository.ITaskRepository

/**
 * Use case to delete a task from the repository.
 */
open class AddTaskUseCase(private val repository: ITaskRepository) {
    /**
     * Invokes the use case to delete a task.
     * @param task The task to delete.
     */
    open suspend operator fun invoke(task: TaskDto) {
        repository.addTask(task)
    }
}