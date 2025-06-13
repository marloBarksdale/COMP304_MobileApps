package com.example.coursemanager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentScreen(
    navController: NavController,
    viewModel: StudentCourseViewModel,
    studentId: Int
) {
    val student = viewModel.getStudent(studentId)
    var name by remember { mutableStateOf(student?.name ?: "") }
    var email by remember { mutableStateOf(student?.email ?: "") }

    if (student == null) {
        Text("Student not found")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Student") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    viewModel.updateStudent(studentId, name, email)
                    navController.popBackStack()
                }) {
                    Text("Save")
                }

                Button(onClick = {
                    viewModel.deleteStudent(studentId)
                    navController.popBackStack("home", inclusive = false)
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Delete")
                }
            }
        }
    }
}