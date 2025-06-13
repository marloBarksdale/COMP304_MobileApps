package com.example.coursemanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(navController: NavController, viewModel: StudentCourseViewModel) {
    val courses = viewModel.courses

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("All Courses") })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
        ) {
            items(courses) { course ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("editCourse/${course.id}")
                        }
                )
                {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Course: ${course.courseName}")
                        Text("Professor: ${course.professor}")
                        Text("Semester: ${course.semester}")
                        Text("Student ID: ${course.studentId ?: "Unassigned"}")
                    }
                }
            }
        }
    }
}