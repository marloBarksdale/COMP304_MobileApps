package com.example.coursemanager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseScreen(
    navController: NavController,
    viewModel: StudentCourseViewModel,
    courseId: Int
) {
    val course = viewModel.getCourse(courseId)

    var name by remember { mutableStateOf(course?.courseName ?: "") }
    var prof by remember { mutableStateOf(course?.professor ?: "") }
    var semester by remember { mutableStateOf(course?.semester ?: "") }

    if (course == null) {
        Text("Course not found.")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Course") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Course Name") })
            OutlinedTextField(value = prof, onValueChange = { prof = it }, label = { Text("Professor") })
            OutlinedTextField(value = semester, onValueChange = { semester = it }, label = { Text("Semester") })

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    viewModel.updateCourse(courseId, name, prof, semester)
                    navController.popBackStack()
                }) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        viewModel.deleteCourse(courseId)
                        navController.popBackStack("courseList", inclusive = false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}