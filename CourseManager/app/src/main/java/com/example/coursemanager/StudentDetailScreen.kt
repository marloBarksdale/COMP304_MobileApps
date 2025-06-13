package com.example.coursemanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    navController: NavController,
    viewModel: StudentCourseViewModel,
    studentId: Int
) {
    val student = viewModel.getStudent(studentId)
    val courses = viewModel.getCoursesForStudent(studentId)

    if (student == null) {
        Text("Student not found.")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${student.name}'s Courses") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("editStudent/${student.id}")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Student")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addCourse/$studentId")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Email: ${student.email}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Courses:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(courses) { course ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Course: ${course.courseName}")
                            Text("Professor: ${course.professor}")
                            Text("Semester: ${course.semester}")
                        }
                    }
                }
            }
        }
    }
}
