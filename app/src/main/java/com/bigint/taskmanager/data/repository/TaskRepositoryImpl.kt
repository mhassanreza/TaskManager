package com.bigint.taskmanager.data.repository

import com.bigint.taskmanager.data.db.TaskDao
import com.bigint.taskmanager.data.db.toEntity
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val dao: TaskDao) : ITaskRepository {
    override suspend fun addTask(task: TaskDto) = dao.insert(task.toEntity())
    override fun getAllTasks(): Flow<List<TaskDto>> =
        dao.getAllTasks().map { it.map { entity -> entity.toDomain() } }

    override suspend fun updateTask(task: TaskDto) = dao.update(task.toEntity())
    override suspend fun deleteTask(task: TaskDto) = dao.delete(task.toEntity())
}