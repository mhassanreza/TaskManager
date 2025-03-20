package com.bigint.taskmanager.domain.model

import com.bigint.taskmanager.domain.enums.Priority


data class TaskDto(
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val priority: Priority,
    val dueDate: Long, // Epoch(is the date and time relative to which a mobile's clock and timestamp values are determined) time in milliseconds
    val isCompleted: Boolean = false
)