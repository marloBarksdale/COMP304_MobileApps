package com.example.lyndenflood_comp304lab1_ex1

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
import androidx.compose.material.icons.filled.Star
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
    private lateinit var editTaskLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = result.data?.getSerializableExtra("newTask", Task::class.java)
                task?.let { taskList.add(it) }
            }
        }
        editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val updatedTask = result.data?.getSerializableExtra("updatedTask", Task::class.java)
                val deletedTaskId = result.data?.getIntExtra("deletedTaskId", -1) ?: -1
                if (updatedTask != null && updatedTask is Task) {
                    // Replace old task with updated one
                    val index = taskList.indexOfFirst { it.id == updatedTask.id }
                    if (index != -1) taskList[index] = updatedTask
                } else if (deletedTaskId != -1) {
                    // Remove task with given id
                    taskList.removeAll { it.id == deletedTaskId }
                }
            }
        }
        setContent {
            TaskListScreen(
                taskList = taskList,
                onTaskClick = { task ->
                    val intent = Intent(this, ViewEditTaskActivity::class.java)
                    intent.putExtra("task", task)
                    editTaskLauncher.launch(intent)
                },
                onAddTask = {
                    val intent = Intent(this, AddTaskActivity::class.java)
                    addTaskLauncher.launch(intent)
                },
                onDeleteTask = { taskList.remove(it) },
                onToggleComplete = { task ->
                    val idx = taskList.indexOf(task)
                    if (idx != -1) {
                        taskList[idx] = task.copy(isComplete = !task.isComplete)
                    }
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
    onDeleteTask: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        if (taskList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("You have no tasks. Add one!", fontSize = 18.sp)
            }
        } else {
            LazyColumn(contentPadding = padding) {
                items(taskList) { task ->
                    TaskCard(
                        task = task,
                        onEdit = { onTaskClick(task) }, // Card click = open edit
                        onToggleComplete = { onToggleComplete(task) }, // Checkbox = toggle complete
                        onDelete = { onDeleteTask(task) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onEdit: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor = if (task.isComplete) Color(0xFFD0F0C0) else Color.White
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onEdit() }, // Card click = edit
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isComplete,
                    onCheckedChange = { onToggleComplete() },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = task.title,
                            style = TextStyle(
                                fontSize = 18.sp,
                                textDecoration = if (task.isComplete) TextDecoration.LineThrough else null
                            )
                        )
                        if (task.isHighPriority) {
                            Icon(Icons.Default.Star, contentDescription = "High Priority", tint = Color(0xFFFFA500), modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                    Text(
                        text = "Due: ${task.dueDate}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
