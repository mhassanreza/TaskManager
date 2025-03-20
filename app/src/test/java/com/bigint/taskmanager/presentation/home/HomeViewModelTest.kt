package com.bigint.taskmanager.presentation.home

import com.bigint.taskmanager.domain.enums.Priority
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.usecase.AddTaskUseCase
import com.bigint.taskmanager.domain.usecase.DeleteTaskUseCase
import com.bigint.taskmanager.domain.usecase.GetTasksUseCase
import com.bigint.taskmanager.presentation.screens.home.intent.HomeIntent
import com.bigint.taskmanager.presentation.screens.home.view_model.HomeViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val getTasksUseCase: GetTasksUseCase = mock()
    private val addTaskUseCase: AddTaskUseCase = mock()
    private val deleteTaskUseCase: DeleteTaskUseCase = mock()

    @Before
    fun setup() {
        // Mock GetTasksUseCase to return an empty list
        whenever(getTasksUseCase()).thenReturn(flowOf(emptyList()))
        viewModel = HomeViewModel(getTasksUseCase, addTaskUseCase, deleteTaskUseCase)
    }

    @Test
    fun `deleteTask calls DeleteTaskUseCase`() = runTest {
        val task = TaskDto(
            id = 1,
            title = "Test Task",
            priority = Priority.MEDIUM,
            dueDate = System.currentTimeMillis()
        )
        viewModel.processIntent(HomeIntent.DeleteTask(task))
        verify(deleteTaskUseCase).invoke(task)
    }

    @Test
    fun `addTask calls AddTaskUseCase`() = runTest {
        val task = TaskDto(
            id = 1,
            title = "Test Task",
            priority = Priority.MEDIUM,
            dueDate = System.currentTimeMillis()
        )
        viewModel.processIntent(HomeIntent.AddTask(task))
        verify(addTaskUseCase).invoke(task)
    }
}