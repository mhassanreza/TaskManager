package com.bigint.taskmanager.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bigint.taskmanager.domain.enums.Priority
import com.bigint.taskmanager.domain.model.TaskDto

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: Priority,
    val dueDate: Long,
    val isCompleted: Boolean
) {
    fun toDomain() = TaskDto(id, title, description, priority, dueDate, isCompleted)
}

fun TaskDto.toEntity() = TaskEntity(id, title, description, priority, dueDate, isCompleted)