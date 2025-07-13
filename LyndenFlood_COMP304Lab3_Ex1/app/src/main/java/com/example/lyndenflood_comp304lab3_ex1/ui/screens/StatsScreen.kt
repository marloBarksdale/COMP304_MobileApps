package com.example.lyndenflood_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lyndenflood_comp304lab3_ex1.viewModel.ActivityViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navController: NavHostController,
    viewModel: ActivityViewModel = viewModel()
) {
    val activities by viewModel.activities.collectAsStateWithLifecycle()

    val totalWorkouts = activities.size
    val totalMinutes = activities.sumOf { it.durationMinutes }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Stats") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Weekly Activity Summary", style = MaterialTheme.typography.titleMedium)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Total Workouts: $totalWorkouts", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Minutes Logged: $totalMinutes", style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Optional: List recent activities
            if (activities.isNotEmpty()) {

                Spacer(modifier = Modifier.height(16.dp))
                Text("Recent Activities:", style = MaterialTheme.typography.titleMedium)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    activities.take(5).forEach { activity ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("edit_activity/${activity.id}")
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(activity.type, style = MaterialTheme.typography.titleSmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${activity.durationMinutes} minutes", style = MaterialTheme.typography.bodyMedium)
                                Text(activity.date, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }


            }
        }
    }
}
