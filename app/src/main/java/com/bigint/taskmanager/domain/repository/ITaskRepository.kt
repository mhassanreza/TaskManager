package com.bigint.taskmanager.domain.repository

import com.bigint.taskmanager.domain.model.TaskDto
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    suspend fun addTask(task: TaskDto)
    fun getAllTasks(): Flow<List<TaskDto>>
    suspend fun updateTask(task: TaskDto)
    suspend fun deleteTask(task: TaskDto)
}
