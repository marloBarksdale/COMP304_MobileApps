package com.example.lyndenflood_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lyndenflood_comp304lab3_ex1.model.FitnessActivity
import com.example.lyndenflood_comp304lab3_ex1.viewModel.ActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivityScreen(
    navController: NavHostController,
    activityId: Int,
    viewModel: ActivityViewModel = viewModel()
) {
    var activity by remember { mutableStateOf<FitnessActivity?>(null) }

    LaunchedEffect(activityId) {
        viewModel.getActivityById(activityId) { result ->
            activity = result
        }
    }

    if (activity == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        var type by remember { mutableStateOf(activity!!.type) }
        var duration by remember { mutableStateOf(activity!!.durationMinutes.toString()) }
        var date by remember { mutableStateOf(activity!!.date) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Edit Activity") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Activity Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.updateActivity(
                                activity!!.copy(
                                    type = type,
                                    durationMinutes = duration.toIntOrNull() ?: 0,
                                    date = date
                                )
                            )
                            navController.navigate("stats") {
                                popUpTo("stats") { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save")
                    }

                    Button(
                        onClick = {
                            viewModel.deleteActivity(activity!!) {
                                navController.navigate("stats") {
                                    popUpTo("stats") { inclusive = true }
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }
}
