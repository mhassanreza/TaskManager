package com.bigint.taskmanager.presentation.screens.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.usecase.AddTaskUseCase
import com.bigint.taskmanager.domain.usecase.DeleteTaskUseCase
import com.bigint.taskmanager.domain.usecase.GetTasksUseCase
import com.bigint.taskmanager.domain.usecase.preferences.PreferencesUseCases
import com.bigint.taskmanager.presentation.screens.home.intent.HomeIntent
import com.bigint.taskmanager.presentation.screens.home.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,    // Added for undo
    private val deleteTaskUseCase: DeleteTaskUseCase // Added for swipe-to-delete
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
//        processIntent(HomeIntent.LoadTasks)
        checkSavedValuesInPreferences()
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadTasks -> loadTasks()
            is HomeIntent.NavigateToTaskCreation -> {} // UI-handled
            is HomeIntent.NavigateToTaskDetails -> {} // UI-handled
            is HomeIntent.UpdateSort -> updateSortOptionPreferences(intent.sortBy)
            is HomeIntent.UpdateFilter -> updateFilterOptionPreferences(intent.filterBy)
            is HomeIntent.DeleteTask -> deleteTask(intent.task)
            is HomeIntent.AddTask -> addTask(intent.task)
            is HomeIntent.UpdateTheme -> updateThemePreferences(intent.isDark)
            is HomeIntent.ReorderTask -> reorderTask(intent.fromIndex, intent.toIndex)
        }
    }

    private fun reorderTask(fromIndex: Int, toIndex: Int) {
        val updatedList = state.value.tasks.toMutableList()
        val movedTask = updatedList.removeAt(fromIndex)
        updatedList.add(toIndex, movedTask)
        _state.value = state.value.copy(tasks = updatedList)
    }


    private fun loadTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getTasksUseCase().map { tasks ->
                val filtered = when (_state.value.filterBy) {
                    FilterOption.ALL -> tasks
                    FilterOption.COMPLETED -> tasks.filter { it.isCompleted }
                    FilterOption.PENDING -> tasks.filter { !it.isCompleted }
                }
                when (_state.value.sortBy) {
                    SortOption.PRIORITY -> filtered.sortedByDescending { it.priority.ordinal }
                    SortOption.DUE_DATE -> filtered.sortedBy { it.dueDate }
                    SortOption.TITLE -> filtered.sortedBy { it.title }
                }
            }.collect { sortedTasks ->
                _state.value = _state.value.copy(tasks = sortedTasks, isLoading = false)
            }
        }
    }

    private fun deleteTask(task: TaskDto) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
            loadTasks() // Refresh list
        }
    }

    private fun addTask(task: TaskDto) {
        viewModelScope.launch {
            addTaskUseCase(task)
            loadTasks() // Refresh list
        }
    }

    private fun checkSavedValuesInPreferences() = viewModelScope.launch {
        combine(
            preferencesUseCases.getCurrentThemeUseCase.invoke(),
            preferencesUseCases.getCurrentSortOptionUseCase.invoke(),
            preferencesUseCases.getCurrentFilterOptionUseCase.invoke()
        ) { isDarkTheme, sortOptionValue, filterOptionValue ->
            Triple(isDarkTheme, sortOptionValue, filterOptionValue)
        }.collect { (isDarkTheme, sortOptionValue, filterOptionValue) ->
            _state.value = _state.value.copy(
                isDarkTheme = isDarkTheme,
                sortBy = SortOption.fromValue(sortOptionValue),
                filterBy = FilterOption.fromValue(filterOptionValue)
            )
            // Load tasks only once after initial values are set
            loadTasks()
        }
//        preferencesUseCases.getCurrentThemeUseCase.invoke().collect {
//            _state.value = _state.value.copy(
//                isDarkTheme = it
//            )
//        }
//        preferencesUseCases.getCurrentSortOptionUseCase.invoke().collect {
//            _state.value = _state.value.copy(
//                sortBy = SortOption.fromValue(it)
//            )
//            processIntent(HomeIntent.LoadTasks)
//        }
//
//        preferencesUseCases.getCurrentFilterOptionUseCase.invoke().collect {
//            println("FilterOption.fromValue(it)-==-=--=-=-=-=${FilterOption.fromValue(it)}-=-==-=-=--==-")
//            _state.value = _state.value.copy(
//                filterBy = FilterOption.fromValue(it)
//            )
//            processIntent(HomeIntent.LoadTasks)
//        }
    }

    private fun updateThemePreferences(isDarkTheme: Boolean) = viewModelScope.launch {
        preferencesUseCases.updateCurrentThemeUseCase(isDarkTheme = isDarkTheme)
    }

    private fun updateSortOptionPreferences(sortBy: SortOption) = viewModelScope.launch {
        preferencesUseCases.updateCurrentSortOptionUseCase(sortOptionValue = sortBy.value)
    }

    private fun updateFilterOptionPreferences(filterOption: FilterOption) = viewModelScope.launch {
        preferencesUseCases.updateFilterOptionUseCase(filterOptionValue = filterOption.value)
    }
}