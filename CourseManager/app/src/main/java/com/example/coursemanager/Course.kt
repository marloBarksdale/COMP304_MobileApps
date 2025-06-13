package com.example.coursemanager

data class Course(
    val id: Int,
    var courseName: String,
    var professor: String,
    var semester: String,
    val studentId: Int? = null
)