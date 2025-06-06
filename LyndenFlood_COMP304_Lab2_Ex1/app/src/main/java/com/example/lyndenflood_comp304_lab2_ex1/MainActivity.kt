package com.example.lyndenflood_comp304_lab2_ex1

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Main entry point of the app
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // State for showing snackbars
                val snackbarHostState = remember { SnackbarHostState() }
                // ViewModel for managing habit data
                val habitViewModel: HabitViewModel = viewModel()
                // Navigation controller for navigating between screens
                val navController = rememberNavController()
                // State to trigger showing a save message
                var showSaveMessage by remember { mutableStateOf(false) }
                // Calculate window size for responsive layouts
                val windowSizeClass = calculateWindowSizeClass(this)

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->
                    // Navigation host to manage different screens
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        // Home screen: displays list of habits
                        composable("home") {
                            val habits by habitViewModel.habits.collectAsState()

                            HabitListScreen(
                                habits = habits,
                                navController = navController, // Pass navigation controller
                                windowWidthSizeClass = windowSizeClass.widthSizeClass,
                                onAddHabitClicked = {
                                    navController.navigate("addHabit")
                                }
                            )
                        }

                        // Edit habit screen: allows editing or deleting a habit
                        composable("editHabit/{habitId}") { backStackEntry ->
                            val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
                            val habit = habitId?.let { habitViewModel.getHabitById(it) }

                            if (habit != null) {
                                EditHabitScreen(
                                    habit = habit,
                                    onSave = { updatedHabit ->
                                        habitViewModel.updateHabit(updatedHabit)
                                        showSaveMessage = true
                                        navController.popBackStack()
                                    },
                                    onCancel = {
                                        navController.popBackStack()
                                    },
                                    onDelete = { habitToDelete ->
                                        habitViewModel.deleteHabit(habitToDelete)
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        // Add habit screen: allows creating a new habit
                        composable("addHabit") {
                            AddHabitScreen(
                                onSave = { habit ->
                                    habitViewModel.addHabit(habit)
                                    showSaveMessage = true    // Set flag to show save message
                                    navController.popBackStack()
                                },
                                onCancel = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Composable for displaying the list of habits
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    habits: List<Habit>,
    navController: NavController,
    onAddHabitClicked: () -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    // Determine if the layout should be tablet or phone style
    val isTabletLayout = windowWidthSizeClass != WindowWidthSizeClass.Compact

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Habit Journey") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (habits.isEmpty()) {
                // Show message if no habits exist
                Text(
                    text = "Grow your habits here!",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            } else {
                // Show list of habits
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(if (isTabletLayout) 24.dp else 8.dp)
                ) {
                    items(habits) { habit ->
                        HabitListItem(
                            habit = habit,
                            onClick = { navController.navigate("editHabit/${habit.id}") },
                            isWide = isTabletLayout
                        )
                    }
                }
            }
        }
    }
}

// Composable for displaying a single habit item in the list
@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitListItem(habit: Habit, onClick: () -> Unit, isWide: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(if (isWide) 16.dp else 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(if (isWide) 8.dp else 4.dp)
    ) {
        Column(modifier = Modifier.padding(if (isWide) 24.dp else 16.dp)) {
            // Display habit name
            Text(text = habit.name, style = MaterialTheme.typography.titleLarge)
            // Display habit frequency
            Text(text = "Frequency: ${habit.frequency}", style = MaterialTheme.typography.bodyLarge)
            // Display progress towards goal
            Text(text = "Progress: ${habit.completedDates.size}/${habit.goal}", style = MaterialTheme.typography.bodyMedium)
            // Calculate and display current streak
            val streak = HabitViewModel().calculateStreak(habit)
            Text(
                text = "Streak: $streak day${if (streak != 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}