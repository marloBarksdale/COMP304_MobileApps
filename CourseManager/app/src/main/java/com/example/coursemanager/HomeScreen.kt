package com.example.coursemanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: StudentCourseViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(

        topBar = {
            TopAppBar(title = { Text("Student Manager") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(text = { Text("Add Student") }, onClick = {
                        showMenu = false
                        navController.navigate("addStudent")
                    })
                    DropdownMenuItem(text = { Text("Add Course") }, onClick = {
                        showMenu = false
                        navController.navigate("addCourse")
                    })
                    DropdownMenuItem(text = { Text("View All Courses") }, onClick = {
                        showMenu = false
                        navController.navigate("courseList")
                    })
                }
            }
        }
    ) { padding ->
        val students = viewModel.students

        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(students) { student ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("studentDetail/${student.id}")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = student.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = student.email, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
