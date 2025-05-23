package com.example.lyndenflood_comp304lab1_ex1

import java.io.Serializable

data class Task(
    val id: Int,
    var title: String,
    var description: String,
    var dueDate: String,
    var isComplete: Boolean = false,
    var isHighPriority: Boolean = false
) : Serializable
