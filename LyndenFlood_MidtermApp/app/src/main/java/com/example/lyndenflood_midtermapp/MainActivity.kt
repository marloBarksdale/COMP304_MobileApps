package com.example.lyndenflood_midtermapp

import android.os.Bundle


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("notes") {
            NotesScreen(navController)
        }
    }
}


@Composable
fun StartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),  // ✅ Make sure logo.png is in drawable
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            navController.navigate("notes")
        }) {
            Text("Go to Notes")
        }
    }
}


@Composable
fun NotesScreen(navController: NavController, notesViewModel: NotesViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    val notes = notesViewModel.notes

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Create a Note", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Priority")

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            listOf("High", "Medium", "Low").forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (priority == it),
                        onClick = { priority = it }
                    )
                    Text(it)
                }
            }
        }

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    notesViewModel.addNote(Note(title, content, priority))
                    title = ""
                    content = ""
                    priority = "Medium"
                }
            },
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text("Add Note")
        }

        Spacer(modifier = Modifier.height(8.dp))

     Text("My Notes:", style = MaterialTheme.typography.titleMedium)




        LazyColumn {
            items(notes.size) { index ->
                val note = notes[index]
                Text("- ${note.title}: ${note.content} [${note.priority}]")
            }
        }


        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Start")
        }
    }
}



