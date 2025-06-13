package com.example.coursemanager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    navController: NavController,
    viewModel: StudentCourseViewModel,
    studentId: Int?
) {
    var courseName by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Course") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = { Text("Course Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = professor,
                onValueChange = { professor = it },
                label = { Text("Professor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = semester,
                onValueChange = { semester = it },
                label = { Text("Semester (e.g., Fall 2025)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                if (courseName.isNotBlank() && professor.isNotBlank() && semester.isNotBlank()) {
                    viewModel.addCourse(courseName, professor, semester, studentId)
                    navController.popBackStack()
                }
            }) {
                Text("Save")
            }
        }
    }
}