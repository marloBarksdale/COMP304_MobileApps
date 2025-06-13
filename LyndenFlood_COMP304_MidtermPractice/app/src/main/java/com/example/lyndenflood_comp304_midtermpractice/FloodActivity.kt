package com.example.lyndenflood_comp304_midtermpractice




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FloodActivityContent(notesViewModel: NotesViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    val notes = notesViewModel.notes

    Column(modifier = Modifier.padding(16.dp)) {
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(8.dp)) {
                    if (title.isEmpty()) Text("Note Title")
                    innerTextField()
                }
            }
        )
        BasicTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(8.dp)) {
                    if (content.isEmpty()) Text("Note Content")
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Select Priority")
        Row {
            RadioButton(selected = (priority == "High"), onClick = { priority = "High" })
            Text("High")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(selected = (priority == "Medium"), onClick = { priority = "Medium" })
            Text("Medium")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(selected = (priority == "Low"), onClick = { priority = "Low" })
            Text("Low")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (title.isNotEmpty() && content.isNotEmpty()) {
                notesViewModel.addNote(Note(title, content, priority))
            }
        }) {
            Text("Add Note")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(notes.size) { index ->
                Text("${notes[index].title}: ${notes[index].content} [${notes[index].priority}]")
            }
        }
    }
}

