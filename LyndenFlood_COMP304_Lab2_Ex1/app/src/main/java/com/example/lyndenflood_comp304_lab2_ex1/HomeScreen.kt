package com.example.lyndenflood_comp304_lab2_ex1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HabitViewModel = getViewModel(),
    onHabitClicked: (Habit) -> Unit,
    onAddHabitClicked: () -> Unit
) {
    val habits by viewModel.habits.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habit Journey") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (habits.isEmpty()) {
                Text(
                    text = "Grow your habits here!",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(habits) { habit ->
                        HabitListItem(habit = habit, onClick = { onHabitClicked(habit) })
                    }
                }
            }
        }
    }
}


@Composable
fun HabitListItem(habit: Habit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Frequency: ${habit.frequency}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Progress: ${habit.completedDates.size}/${habit.goal}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

