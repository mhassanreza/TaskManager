package com.bigint.taskmanager.presentation.screens.task_creation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigint.taskmanager.R
import com.bigint.taskmanager.domain.enums.Priority
import com.bigint.taskmanager.presentation.screens.task_creation.intent.TaskCreationIntent
import com.bigint.taskmanager.presentation.screens.task_creation.view_model.TaskCreationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(
    goBackToHome: () -> Unit,
    viewModel: TaskCreationViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    if (state.isSaved) {
        LaunchedEffect(Unit) {
            goBackToHome.invoke()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_task_label)) },
                navigationIcon = {
                    IconButton(onClick = { goBackToHome.invoke() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.processIntent(TaskCreationIntent.UpdateTitle(it)) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.processIntent(TaskCreationIntent.UpdateDescription(it)) },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            PriorityDropdown(
                selectedPriority = state.priority,
                onPrioritySelected = { viewModel.processIntent(TaskCreationIntent.UpdatePriority(it)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            DueDatePicker(
                dueDate = state.dueDate,
                onDateSelected = { viewModel.processIntent(TaskCreationIntent.UpdateDueDate(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.processIntent(TaskCreationIntent.SaveTask) },
                enabled = state.title.isNotBlank() && !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isSaving) CircularProgressIndicator() else Text(stringResource(R.string.save_task_label))
            }
        }
    }
}

@Composable
fun PriorityDropdown(selectedPriority: Priority, onPrioritySelected: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedPriority.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Priority") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { IconButton(onClick = { expanded = true }) { Text("▼") } }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePicker(dueDate: Long, onDateSelected: (Long) -> Unit) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance().apply { timeInMillis = dueDate }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)

    OutlinedTextField(
        value = formatter.format(calendar.time),
        onValueChange = {},
        readOnly = true,
        label = { Text("Due Date") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate ->
                        onDateSelected(selectedDate)
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok_label))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel_label))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
