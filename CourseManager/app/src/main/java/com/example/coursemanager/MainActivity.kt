package com.example.coursemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudentCourseApp()
            }
        }
    }
}

@Composable
fun StudentCourseApp(viewModel: StudentCourseViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {

        composable(
            "editStudent/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt("studentId") ?: return@composable
            EditStudentScreen(navController, viewModel, studentId)
        }

        composable("addCourse") {
            AddCourseScreen(navController, viewModel, null)
        }

        composable("courseList") {
            CourseListScreen(navController, viewModel)
        }
        composable(
            "editCourse/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            val courseId = it.arguments?.getInt("courseId") ?: return@composable
            EditCourseScreen(navController, viewModel, courseId)
        }


        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("addStudent") {
            AddStudentScreen(navController, viewModel)
        }
        composable(
            "studentDetail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt("studentId") ?: return@composable
            StudentDetailScreen(navController, viewModel, studentId)
        }
    }
}