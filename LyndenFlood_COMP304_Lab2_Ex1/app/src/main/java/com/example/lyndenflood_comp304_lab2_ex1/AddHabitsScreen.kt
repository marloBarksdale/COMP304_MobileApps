
package com.example.lyndenflood_comp304_lab2_ex1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddHabitScreen(
    onSave: (Habit) -> Unit, // Callback when the user saves the new habit
    onCancel: () -> Unit     // Callback when the user cancels
) {
    // State for the habit name input
    var name by remember { mutableStateOf("") }
    // State for the goal input (as string for text field)
    var goal by remember { mutableStateOf("") }
    // State for the selected frequency (default: DAILY)
    var selectedFrequency by remember { mutableStateOf(Frequency.DAILY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title of the screen
        Text("Add New Habit", style = MaterialTheme.typography.headlineSmall)

        // Input field for the habit name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Input field for the goal (number of times per period)
        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Goal (times per period)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            )
        )

        // Dropdown for selecting the frequency (daily, weekly, etc.)
        FrequencyDropdown(selected = selectedFrequency, onSelected = { selectedFrequency = it })

        // Row with Cancel and Save buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Cancel button: calls onCancel callback
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            // Save button: creates a new Habit and calls onSave callback
            Button(
                onClick = {
                    val habit = Habit(
                        id = (0..100000).random(), // Generate a random id
                        name = name,
                        description = "",
                        startDate = LocalDate.now(),
                        frequency = selectedFrequency,
                        goal = goal.toIntOrNull() ?: 1, // Default to 1 if input is invalid
                        completedDates = emptyList()
                    )
                    onSave(habit)
                },
                enabled = name.isNotBlank() && goal.toIntOrNull() != null // Enable only if valid input
            ) {
                Text("Save")
            }
        }
    }
}

// Dropdown menu for selecting habit frequency
@Composable
fun FrequencyDropdown(
    selected: Frequency,                // Currently selected frequency
    onSelected: (Frequency) -> Unit     // Callback when a frequency is selected
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Button to open the dropdown menu
        OutlinedButton(onClick = { expanded = true }) {
            Text("Frequency: ${selected.name}")
        }
        // Dropdown menu listing all frequency options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Frequency.values().forEach { freq ->
                DropdownMenuItem(
                    text = { Text(freq.name) },
                    onClick = {
                        onSelected(freq)
                        expanded = false
                    }
                )
            }
        }
    }
}