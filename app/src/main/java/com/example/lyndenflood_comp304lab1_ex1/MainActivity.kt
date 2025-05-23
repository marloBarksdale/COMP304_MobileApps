package com.example.lyndenflood_comp304lab1_ex1

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val taskList = mutableStateListOf<Task>()

    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = result.data?.getSerializableExtra("newTask", Task::class.java)
                task?.let { taskList.add(it) }
            }
        }

        setContent {
            TaskListScreen(
                taskList = taskList,
                onTaskClick = { task ->
                    val intent = Intent(this, ViewEditTaskActivity::class.java)
                    intent.putExtra("task", task)
                    startActivity(intent)
                },
                onAddTask = {
                    val intent = Intent(this, AddTaskActivity::class.java)
                    addTaskLauncher.launch(intent)
                },
                onDeleteTask = { task ->
                    taskList.remove(task)
                }
            )
        }
    }
}

@Composable
fun TaskListScreen(
    taskList: List<Task>,
    onTaskClick: (Task) -> Unit,
    onAddTask: () -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) {
        if (taskList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("You have no tasks. Add one!", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(taskList) { task ->
                    TaskCard(task = task, onClick = { onTaskClick(task) }, onDelete = { onDeleteTask(task) })
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit, onDelete: () -> Unit) {
    val bgColor = if (task.isComplete) Color(0xFFD0F0C0) else Color.White
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        textDecoration = if (task.isComplete) TextDecoration.LineThrough else null
                    )
                )
                Text(text = "Due: ${task.dueDate}", fontSize = 14.sp, color = Color.Gray)
                if (task.isHighPriority) Text("High Priority", color = Color.Red, fontSize = 12.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
