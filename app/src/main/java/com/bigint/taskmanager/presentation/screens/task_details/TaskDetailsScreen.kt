package com.bigint.taskmanager.presentation.screens.task_details


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigint.taskmanager.R
import com.bigint.taskmanager.presentation.screens.task_details.intent.TaskDetailsIntent
import com.bigint.taskmanager.presentation.screens.task_details.view_model.TaskDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    goBackToHome: () -> Unit,
    taskId: Int,
    viewModel: TaskDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(taskId) { viewModel.processIntent(TaskDetailsIntent.LoadTask(taskId)) }

    if (state.isDeleted) {
        LaunchedEffect(Unit) { goBackToHome.invoke() }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_details_label)) },
                navigationIcon = {
                    IconButton(onClick = { goBackToHome.invoke() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading || state.task == null) {
            CircularProgressIndicator(modifier = Modifier.padding(padding))
        } else {
            val task = state.task
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = task.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stringResource(R.string.label_description)}: ${task.description ?: "None"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${stringResource(R.string.label_priority)}: ${task.priority.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${stringResource(R.string.label_due)}: ${formatter.format(Date(task.dueDate))}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${stringResource(R.string.label_status)}: ${
                        if (task.isCompleted) stringResource(R.string.complete_label) else stringResource(
                            R.string.pending_label
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.processIntent(TaskDetailsIntent.MarkComplete(task)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (task.isCompleted) stringResource(R.string.mark_incomplete_label) else stringResource(
                            R.string.mark_complete_label
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.processIntent(TaskDetailsIntent.DeleteTask(task)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete_task_label))
                }
            }
        }
    }
}