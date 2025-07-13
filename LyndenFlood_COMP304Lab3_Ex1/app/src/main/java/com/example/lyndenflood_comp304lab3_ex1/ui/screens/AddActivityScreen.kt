package com.example.lyndenflood_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lyndenflood_comp304lab3_ex1.viewModel.ActivityViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    navController: NavHostController,
    viewModel: ActivityViewModel = viewModel()
) {
    var activityType by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Activity") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Enter activity details", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = activityType,
                onValueChange = { activityType = it },
                label = { Text("Activity Type (e.g., Running)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = duration,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        duration = input
                    }
                },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (activityType.isNotBlank() && duration.isNotBlank()) {
                        viewModel.addActivity(
                            type = activityType,
                            duration = duration.toInt(),
                            date = LocalDate.now().toString()
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save")
            }
        }
    }
}
