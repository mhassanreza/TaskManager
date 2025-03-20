package com.bigint.taskmanager.presentation.screens.task_details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.usecase.DeleteTaskUseCase
import com.bigint.taskmanager.domain.usecase.GetTasksUseCase
import com.bigint.taskmanager.domain.usecase.UpdateTaskUseCase
import com.bigint.taskmanager.presentation.screens.task_details.intent.TaskDetailsIntent
import com.bigint.taskmanager.presentation.screens.task_details.state.TaskDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TaskDetailsState())
    val state: StateFlow<TaskDetailsState> = _state.asStateFlow()

    fun processIntent(intent: TaskDetailsIntent) {
        when (intent) {
            is TaskDetailsIntent.LoadTask -> loadTask(intent.taskId)
            is TaskDetailsIntent.MarkComplete -> markComplete(intent.task)
            is TaskDetailsIntent.DeleteTask -> deleteTask(intent.task)
        }
    }

    private fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getTasksUseCase().collect { tasks ->
                val task = tasks.find { it.id == taskId }
                _state.value = _state.value.copy(task = task, isLoading = false)
            }
        }
    }

    private fun markComplete(task: TaskDto) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(isCompleted = !task.isCompleted))
            loadTask(task.id) // Refresh task
        }
    }

    private fun deleteTask(task: TaskDto) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
            _state.value = _state.value.copy(isDeleted = true)
        }
    }
}