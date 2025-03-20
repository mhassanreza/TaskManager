package com.bigint.taskmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.domain.repository.ITaskRepository
import com.bigint.taskmanager.domain.usecase.AddTaskUseCase
import com.bigint.taskmanager.domain.usecase.DeleteTaskUseCase
import com.bigint.taskmanager.domain.usecase.GetTasksUseCase
import com.bigint.taskmanager.presentation.screens.home.HomeScreen
import com.bigint.taskmanager.presentation.screens.home.view_model.HomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testEmptyStateDisplayed() {
        composeTestRule.setContent {
            TestHomeScreenWrapper()
        }
        composeTestRule.onNodeWithText("No tasks yet!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap + to add a task").assertIsDisplayed()
    }

    @Test
    fun testFabClickNavigates() {
        var navigated = false
        composeTestRule.setContent {
            TestHomeScreenWrapper(goToTaskCreationScreen = { navigated = true })
        }
        composeTestRule.onNodeWithContentDescription("Rotating FAB").performClick()
        composeTestRule.waitForIdle() // Wait for coroutines and animations
        assert(navigated)
    }
}

@Composable
private fun TestHomeScreenWrapper(
    onThemeToggle: () -> Unit = {},
    goToTaskCreationScreen: () -> Unit = {},
    goToTaskDetailScreen: (Int) -> Unit = {},
    goToSettingsScreen: () -> Unit = {}
) {
//    HomeScreen(
//        goToTaskCreationScreen = goToTaskCreationScreen,
//        goToTaskDetailScreen = goToTaskDetailScreen,
//        goToSettingsScreen = goToSettingsScreen,
//        viewModel = FakeHomeViewModel()
//    )
}

// Fake ITaskRepository implementation
private class FakeITaskRepository : ITaskRepository {
    override fun getAllTasks(): Flow<List<TaskDto>> = flowOf(emptyList())
    override suspend fun updateTask(task: TaskDto) {
    }

    override suspend fun addTask(task: TaskDto) {}
    override suspend fun deleteTask(task: TaskDto) {}
}

// Fake implementations for use cases
private class FakeGetTasksUseCase(repository: ITaskRepository) : GetTasksUseCase(repository) {
    override fun invoke(): Flow<List<TaskDto>> = flowOf(emptyList())
}

private class FakeAddTaskUseCase(repository: ITaskRepository) : AddTaskUseCase(repository) {
    override suspend fun invoke(task: TaskDto) {}
}

private class FakeDeleteTaskUseCase(repository: ITaskRepository) : DeleteTaskUseCase(repository) {
    override suspend fun invoke(task: TaskDto) {}
}

//// Fake ViewModel using the fake use cases
//class FakeHomeViewModel : HomeViewModel(
//    preferencesUseCases = FakePreferencesUseCases(),
//    getTasksUseCase = FakeGetTasksUseCase(FakeITaskRepository()),
//    addTaskUseCase = FakeAddTaskUseCase(FakeITaskRepository()),
//    deleteTaskUseCase = FakeDeleteTaskUseCase(FakeITaskRepository())
//)