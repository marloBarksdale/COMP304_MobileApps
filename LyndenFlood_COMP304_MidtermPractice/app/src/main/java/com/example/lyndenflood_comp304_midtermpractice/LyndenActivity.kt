package com.example.lyndenflood_comp304_midtermpractice



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class LyndenActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesApp()
        }
    }
}

@Composable
fun MyNotesApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lyndenActivity") {
        composable("lyndenActivity") {
            LyndenActivityContent {
                navController.navigate("floodActivity")
            }
        }
        composable("floodActivity") {
            FloodActivityContent()
        }
    }
}

@Composable
fun LyndenActivityContent(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo image resource
            contentDescription = "App Logo",
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigate) {
            Text("Go to MyNotes")
        }
    }
}