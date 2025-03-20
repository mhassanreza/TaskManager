package com.bigint.taskmanager.presentation.screens.task_creation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.usecase.AddTaskUseCase
import com.bigint.taskmanager.presentation.screens.task_creation.intent.TaskCreationIntent
import com.bigint.taskmanager.presentation.screens.task_creation.state.TaskCreationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskCreationViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskCreationState())
    val state: StateFlow<TaskCreationState> = _state.asStateFlow()

    fun processIntent(intent: TaskCreationIntent) {
        when (intent) {
            is TaskCreationIntent.UpdateTitle -> _state.value =
                _state.value.copy(title = intent.title)

            is TaskCreationIntent.UpdateDescription -> _state.value =
                _state.value.copy(description = intent.description)

            is TaskCreationIntent.UpdatePriority -> _state.value =
                _state.value.copy(priority = intent.priority)

            is TaskCreationIntent.UpdateDueDate -> _state.value =
                _state.value.copy(dueDate = intent.dueDate)

            is TaskCreationIntent.SaveTask -> saveTask()
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            val task = TaskDto(
                title = _state.value.title,
                description = _state.value.description.takeIf { it.isNotBlank() },
                priority = _state.value.priority,
                dueDate = _state.value.dueDate
            )
            addTaskUseCase(task)
            _state.value = _state.value.copy(isSaving = false, isSaved = true)
        }
    }
}