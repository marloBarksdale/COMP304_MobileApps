package com.example.lyndenflood_comp304_lab2_ex1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditHabitScreen(
    habit: Habit,
    onSave: (Habit) -> Unit,
    onCancel: () -> Unit,
    onDelete: (Habit) -> Unit
) {
    // State for editable fields, initialized with current habit values
    var name by remember { mutableStateOf(habit.name) }
    var goal by remember { mutableStateOf(habit.goal.toString()) }
    var selectedFrequency by remember { mutableStateOf(habit.frequency ?: Frequency.DAILY) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen title
        Text("Edit Habit", style = MaterialTheme.typography.headlineSmall)

        // Text field for habit name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Text field for goal (number input)
        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Goal (times per period)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            )
        )

        // Dropdown for selecting frequency (daily, weekly, etc.)
        FrequencyDropdown(selected = selectedFrequency, onSelected = { selectedFrequency = it })

        // Row with Cancel and Save buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Cancel button returns to previous screen
            Button(onClick = onCancel) {
                Text("Cancel")
            }

            // Save button updates the habit with new values
            Button(
                onClick = {
                    val updated = habit.copy(
                        name = name,
                        goal = goal.toIntOrNull() ?: habit.goal,
                        frequency = selectedFrequency
                    )
                    onSave(updated)
                },
                enabled = name.isNotBlank() && goal.toIntOrNull() != null
            ) {
                Text("Save")
            }
        }

        // Check if today's date is already marked as completed
        val todayMarked = habit.completedDates.contains(LocalDate.now())

        // Button to mark the habit as done for today
        Button(
            onClick = {
                val updated = habit.copy(
                    completedDates = habit.completedDates + LocalDate.now()
                )
                onSave(updated)
            },
            enabled = !todayMarked, // Disable if already marked today
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mark as Done Today")
        }

        // Spacer for layout separation
        Spacer(modifier = Modifier.height(16.dp))

        // Button to trigger delete confirmation dialog
        Button(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Habit", color = MaterialTheme.colorScheme.onError)
        }

        // Confirmation dialog for deleting the habit
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        onDelete(habit)
                        showDeleteDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this habit? This action cannot be undone.") }
            )
        }
    }
}