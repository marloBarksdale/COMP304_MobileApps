package com.example.lyndenflood_comp304lab1_ex1

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ViewEditTaskActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task = intent.getSerializableExtra("task", Task::class.java)
        setContent {
            task?.let {
                EditTaskScreen(
                    task = it,
                    onSave = { updatedTask ->
                        val resultIntent = Intent()
                        resultIntent.putExtra("updatedTask", updatedTask)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    },
                    onDelete = { deletedTask ->
                        val resultIntent = Intent()
                        resultIntent.putExtra("deletedTaskId", deletedTask.id)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun EditTaskScreen(task: Task, onSave: (Task) -> Unit, onDelete: (Task) -> Unit) {
    var title by remember { mutableStateOf(task.title) }
    var desc by remember { mutableStateOf(task.description) }
    var date by remember { mutableStateOf(task.dueDate) }
    var isComplete by remember { mutableStateOf(task.isComplete) }
    var isHighPriority by remember { mutableStateOf(task.isHighPriority) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
        TextField(value = date, onValueChange = { date = it }, label = { Text("Due Date") })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isComplete, onCheckedChange = { isComplete = it })
            Text("Completed")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isHighPriority, onCheckedChange = { isHighPriority = it })
            Text("High Priority")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            onSave(
                task.copy(
                    title = title,
                    description = desc,
                    dueDate = date,
                    isComplete = isComplete,
                    isHighPriority = isHighPriority
                )
            )
        }) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onDelete(task) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Delete Task", color = Color.White)
        }
    }
}
