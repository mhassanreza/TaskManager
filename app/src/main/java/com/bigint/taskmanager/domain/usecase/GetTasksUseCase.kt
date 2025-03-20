package com.bigint.taskmanager.domain.usecase

import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to retrieve all tasks from the repository.
 * Returns a Flow to allow reactive updates in the UI.
 */
open class GetTasksUseCase(private val repository: ITaskRepository) {
    /**
     * Invokes the use case to fetch all tasks.
     * @return Flow of List<Task> for real-time updates.
     */
    open operator fun invoke(): Flow<List<TaskDto>> {
        return repository.getAllTasks()
    }
}