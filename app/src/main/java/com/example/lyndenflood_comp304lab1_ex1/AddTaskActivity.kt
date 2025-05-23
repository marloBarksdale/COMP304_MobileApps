package com.example.lyndenflood_comp304lab1_ex1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class AddTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddTaskScreen(onSave = { task ->
                val resultIntent = Intent()
                resultIntent.putExtra("newTask", task)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            })
        }
    }
}

@Composable
fun AddTaskScreen(onSave: (Task) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isHighPriority by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
        TextField(value = date, onValueChange = { date = it }, label = { Text("Due Date") })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isHighPriority, onCheckedChange = { isHighPriority = it })
            Text("High Priority")
        }
        Button(onClick = {
            val newTask = Task(
                id = Random.nextInt(),
                title = title,
                description = desc,
                dueDate = date,
                isHighPriority = isHighPriority
            )
            onSave(newTask)
        }) {
            Text("Save")
        }
    }
}