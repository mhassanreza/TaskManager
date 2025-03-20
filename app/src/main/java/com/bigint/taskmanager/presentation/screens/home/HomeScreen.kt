package com.bigint.taskmanager.presentation.screens.home


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigint.taskmanager.R
import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.model.TaskDto
import com.bigint.taskmanager.presentation.screens.common_components.RotatingFAB
import com.bigint.taskmanager.presentation.screens.home.intent.HomeIntent
import com.bigint.taskmanager.presentation.screens.home.view_model.HomeViewModel
import com.bigint.taskmanager.presentation.ui.theme.TaskManagerAppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToTaskCreationScreen: () -> Unit,
    goToSettingsScreen: () -> Unit,
    goToTaskDetailScreen: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_manager_label)) },
                actions = {
                    IconButton(onClick = {
                        viewModel.processIntent(HomeIntent.UpdateTheme(!state.isDarkTheme))
                    }) {
                        Icon(
                            imageVector = if (!state.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Switch to ${if (!state.isDarkTheme) "Light" else "Dark"} Theme"
                        )
                    }
                    IconButton(onClick = { goToSettingsScreen.invoke() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            RotatingFAB {
                goToTaskCreationScreen()
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SortFilterControls(
                sortBy = state.sortBy,
                filterBy = state.filterBy,
                onSortChange = { viewModel.processIntent(HomeIntent.UpdateSort(it)) },
                onFilterChange = { viewModel.processIntent(HomeIntent.UpdateFilter(it)) }
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier)
            } else if (state.tasks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No tasks yet!", style = MaterialTheme.typography.headlineSmall)
                    Text("Tap + to add a task", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                TaskList(
                    modifier = Modifier,
                    lazyListState = lazyListState,
                    tasks = state.tasks,
                    onTaskClick = { task ->
                        viewModel.processIntent(HomeIntent.NavigateToTaskDetails(task.id))
                        goToTaskDetailScreen(task.id)
                    },
                    onDeleteTask = { task ->
                        viewModel.processIntent(HomeIntent.DeleteTask(task))
                        coroutineScope.launch {
                            val result = snackBarHostState.showSnackbar(
                                message = "Task deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.processIntent(HomeIntent.AddTask(task))
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    tasks: List<TaskDto>,
    onTaskClick: (TaskDto) -> Unit,
    onDeleteTask: (TaskDto) -> Unit
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier.animateContentSize()
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            Modifier
                .fillMaxWidth()
            Box(
                modifier = Modifier.animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null,
                    placementSpec = tween(durationMillis = 700)
                ) // Animate new items
            ) {
                TasksListItemWithSwipe(
                    task = task,
                    onTaskClick = onTaskClick,
                    onDeleteTask = onDeleteTask
                )
            }
        }
    }
}

@Composable
fun TasksListItemWithSwipe(
    task: TaskDto,
    onTaskClick: (TaskDto) -> Unit,
    onDeleteTask: (TaskDto) -> Unit
) {
    val currentItem by rememberUpdatedState(task)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteTask(currentItem)
                    true
                }

                else -> false
            }
        },
        positionalThreshold = { it * 0.5f }
    )

    SwipeToDismissBox(
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        state = dismissState,
        modifier = Modifier,
        backgroundContent = {
            SwipeBackgroundWithEditAndDelete(dismissState)
        },
        content = {
            TasksBasicListItemCard(task, onTaskClick)
        }
    )
}

@Composable
fun SwipeBackgroundWithEditAndDelete(
    dismissState: SwipeToDismissBoxState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier)
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            DecorateTextForSwipeBackground(color = Color(0xFFFF1744), text = "Delete")
        }
    }
}

@Composable
fun DecorateTextForSwipeBackground(
    color: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(100.dp)
            .background(color, shape = MaterialTheme.shapes.extraLarge)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TasksBasicListItemCard(
    task: TaskDto,
    onTaskClick: (TaskDto) -> Unit
) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    ElevatedCard(
        onClick = { onTaskClick(task) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { onTaskClick(task) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = "${stringResource(R.string.label_priority)}: ${task.priority.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${stringResource(R.string.label_due)}: ${formatter.format(Date(task.dueDate))}",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                StatusIndicator(status = if (task.isCompleted) FilterOption.COMPLETED else FilterOption.PENDING)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (task.isCompleted) stringResource(R.string.complete_label) else stringResource(
                        R.string.pending_label
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: FilterOption) {
    val color = when (status) {
        FilterOption.COMPLETED -> Color.Green
        FilterOption.PENDING -> Color.Yellow
        FilterOption.ALL -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color, shape = CircleShape)
    )
}

@Composable
fun SortFilterControls(
    sortBy: SortOption,
    filterBy: FilterOption,
    onSortChange: (SortOption) -> Unit,
    onFilterChange: (FilterOption) -> Unit
) {
    var sortExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            TextButton(onClick = { sortExpanded = true }) { Text("Sort: $sortBy") }
            DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = { onSortChange(option); sortExpanded = false }
                    )
                }
            }
        }
        VerticalDivider(modifier = Modifier.height(20.dp))
        Box {
            TextButton(onClick = { filterExpanded = true }) { Text("Filter: $filterBy") }
            DropdownMenu(expanded = filterExpanded, onDismissRequest = { filterExpanded = false }) {
                FilterOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = { onFilterChange(option); filterExpanded = false }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskManagerAppTheme {
        HomeScreen(
            goToTaskDetailScreen = {},
            goToTaskCreationScreen = {},
            goToSettingsScreen = {}
        )
    }
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    goToTaskCreationScreen: () -> Unit,
//    goToSettingsScreen: () -> Unit,
//    goToTaskDetailScreen: (Int) -> Unit,
//    viewModel: HomeViewModel = hiltViewModel()
//) {
//    val state = viewModel.state.collectAsState().value
//    // Scroll state for the lazy column
//    val lazyListState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//    val snackBarHostState = remember { SnackbarHostState() }
//    Scaffold(topBar = {
//        TopAppBar(title = { Text(stringResource(R.string.task_manager_label)) }, actions = {
//            IconButton(onClick = {
//                viewModel.processIntent(HomeIntent.UpdateTheme(!state.isDarkTheme))
//            }) {
//                Icon(
//                    imageVector = if (!state.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
//                    contentDescription = "Switch to ${if (!state.isDarkTheme) "Light" else "Dark"} Theme"
//                )
//            }
//            IconButton(onClick = { goToSettingsScreen.invoke() }) {
//                Icon(Icons.Default.Settings, contentDescription = "Settings")
//            }
//        })
//    }, floatingActionButton = {
//        RotatingFAB {
//            goToTaskCreationScreen()
//        }
//    }, snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
//        Column(
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            SortFilterControls(
//                sortBy = state.sortBy,
//                filterBy = state.filterBy,
//                onSortChange = { viewModel.processIntent(HomeIntent.UpdateSort(it)) },
//                onFilterChange = { viewModel.processIntent(HomeIntent.UpdateFilter(it)) })
//            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
//            if (state.isLoading) {
//                CircularProgressIndicator(modifier = Modifier)
//            } else if (state.tasks.isEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text("No tasks yet!", style = MaterialTheme.typography.headlineSmall)
//                    Text("Tap + to add a task", style = MaterialTheme.typography.bodyMedium)
//                }
//            } else {
//                TaskList(
//                    modifier = Modifier,
//                    lazyListState = lazyListState,
//                    tasks = state.tasks,
//                    onTaskClick = { task ->
//                        viewModel.processIntent(HomeIntent.NavigateToTaskDetails(task.id))
//                        goToTaskDetailScreen(task.id)
//                    },
//                    onDeleteTask = { task ->
//                        viewModel.processIntent(HomeIntent.DeleteTask(task))
//                        coroutineScope.launch {
//                            val result = snackBarHostState.showSnackbar(
//                                message = "Task deleted",
//                                actionLabel = "Undo",
//                                duration = SnackbarDuration.Short
//                            )
//                            if (result == SnackbarResult.ActionPerformed) {
//                                viewModel.processIntent(HomeIntent.AddTask(task)) // Re-add task
//                            }
//                        }
//                    })
//            }
//        }
//    }
//}
//
//@Composable
//fun TaskList(
//    modifier: Modifier = Modifier,
//    lazyListState: LazyListState,
//    tasks: List<TaskDto>,
//    onTaskClick: (TaskDto) -> Unit,
//    onDeleteTask: (TaskDto) -> Unit,
//) {
//    LazyColumn(
//        state = lazyListState,
//        modifier = modifier.animateContentSize() // Smooth container resize
//    ) {
//        items(tasks, key = { it.id }) { task ->
//            TasksListItemWithSwipe(
//                task,
//                onTaskClick = { onTaskClick(it) },
//                onDeleteTask = { onDeleteTask(it) })
//        }
//    }
//}
//
//@Composable
//fun TasksListItemWithSwipe(
//    task: TaskDto,
//    onTaskClick: (TaskDto) -> Unit,
//    onDeleteTask: (TaskDto) -> Unit,
//) {
//    val currentItem by rememberUpdatedState(task)
//    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
//        when (value) {
//            SwipeToDismissBoxValue.EndToStart -> {
//                onDeleteTask(currentItem) // Trigger deletion
//                true // Allow the swipe to complete, so HomeScreen can show Snackbar
//            }
//
//            else -> false // Reset for other directions
//        }
//    }, positionalThreshold = { it * 0.5f } // 50% threshold
//    )
//
//    SwipeToDismissBox(
//        enableDismissFromEndToStart = true,
//        enableDismissFromStartToEnd = false,
//        state = dismissState,
//        modifier = Modifier,
//        backgroundContent = {
//            SwipeBackgroundWithEditAndDelete(dismissState)
//        },
//        content = {
//            TasksBasicListItemCard(task, onTaskClick)
//        })
//
//}
//
//@Composable
//fun SwipeBackgroundWithEditAndDelete(
//    dismissState: SwipeToDismissBoxState,
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Transparent) // Keep the background transparent when settled
//            .padding(horizontal = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Spacer(modifier = Modifier)
//        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
//            DecorateTextForSwipeBackground(color = Color(0xFFFF1744), text = "Delete")
//        }
//    }
//}
//
//@Composable
//fun DecorateTextForSwipeBackground(
//    color: Color, text: String, modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .width(100.dp)
//            .background(color, shape = MaterialTheme.shapes.extraLarge)
//            .padding(horizontal = 16.dp, vertical = 8.dp), contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text, color = Color.White, style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}
//
//@Composable
//fun TasksBasicListItemCard(
//    task: TaskDto,
//    onTaskClick: (TaskDto) -> Unit,
//) {
//    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
//
//    ElevatedCard(
//        onClick = { onTaskClick(task) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable(
//                onClick = { onTaskClick(task) },
//            ),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//        ),
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = task.title,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.weight(1f) // Ensures title takes remaining space
//                )
//            }
//            Text(
//                text = "${stringResource(R.string.label_priority)}: ${task.priority.name}",
//                style = MaterialTheme.typography.bodySmall
//            )
//            Text(
//                text = "${stringResource(R.string.label_due)}: ${
//                    formatter.format(Date(task.dueDate))
//                }",
//                style = MaterialTheme.typography.bodySmall
//            )
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Status Indicator (colored dot)
//                Spacer(modifier = Modifier.weight(1f)) // Space between dot and title
//                StatusIndicator(status = if (task.isCompleted) FilterOption.COMPLETED else FilterOption.PENDING)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = if (task.isCompleted) stringResource(R.string.complete_label) else stringResource(
//                        R.string.pending_label
//                    ),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
//
//// Assuming TaskDto has a status property, e.g., an enum
//@Composable
//fun StatusIndicator(status: FilterOption) {
//    val color = when (status) {
//        FilterOption.COMPLETED -> Color.Green
//        FilterOption.PENDING -> Color.Yellow
//        // Add more statuses as needed
//        FilterOption.ALL -> Color.Transparent
//    }
//    Box(
//        modifier = Modifier
//            .size(12.dp) // Size of the dot
//            .background(color, shape = CircleShape) // Colored circle
//    )
//}
//
//@Composable
//fun SortFilterControls(
//    sortBy: SortOption,
//    filterBy: FilterOption,
//    onSortChange: (SortOption) -> Unit,
//    onFilterChange: (FilterOption) -> Unit
//) {
//    var sortExpanded by remember { mutableStateOf(false) }
//    var filterExpanded by remember { mutableStateOf(false) }
//
//    Row(
//        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box {
//            TextButton(onClick = { sortExpanded = true }) { Text("Sort: $sortBy") }
//            DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
//                SortOption.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = { onSortChange(option); sortExpanded = false })
//                }
//            }
//        }
//        VerticalDivider(modifier = Modifier.height(20.dp))
//        Box {
//            TextButton(onClick = { filterExpanded = true }) { Text("Filter: $filterBy") }
//            DropdownMenu(expanded = filterExpanded, onDismissRequest = { filterExpanded = false }) {
//                FilterOption.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = { onFilterChange(option); filterExpanded = false })
//                }
//            }
//        }
//    }
//}
//
///*  This preview uses a ViewModel.
//    ViewModels often trigger operations not supported by Compose Preview,
//    such as database access, I/ O operations, or network requests.
//    You can read more about preview limitations in our external documentation. */
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TaskManagerAppTheme {
//        HomeScreen(goToTaskDetailScreen = {}, goToTaskCreationScreen = {}, goToSettingsScreen = {})
//    }
//}
