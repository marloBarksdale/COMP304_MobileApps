package com.example.coursemanager


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class StudentCourseViewModel : ViewModel() {

    private var studentIdCounter = 1
    private var courseIdCounter = 1

    private val _students = mutableStateListOf<Student>()
    val students: List<Student> get() = _students

    private val _courses = mutableStateListOf<Course>()
    val courses: List<Course> get() = _courses

    // ----------- STUDENTS CRUD -----------
    fun addStudent(name: String, email: String) {
        _students.add(Student(studentIdCounter++, name, email))
    }

    fun updateStudent(id: Int, name: String, email: String) {
        _students.find { it.id == id }?.apply {
            this.name = name
            this.email = email
        }
    }

    fun deleteStudent(id: Int) {
        _students.removeAll { it.id == id }
        _courses.removeAll { it.studentId == id } // also remove related courses
    }

    fun getStudent(id: Int): Student? = _students.find { it.id == id }

    // ----------- COURSES CRUD -----------
    fun addCourse(courseName: String, professor: String, semester: String, studentId: Int? = null) {
        _courses.add(Course(courseIdCounter++, courseName, professor, semester, studentId))
    }


    fun updateCourse(id: Int, courseName: String, professor: String, semester: String) {
        _courses.find { it.id == id }?.apply {
            this.courseName = courseName
            this.professor = professor
            this.semester = semester
        }
    }

    fun deleteCourse(id: Int) {
        _courses.removeAll { it.id == id }
    }

    fun getCourse(id: Int): Course? = _courses.find { it.id == id }

    fun getCoursesForStudent(studentId: Int): List<Course> =
        _courses.filter { it.studentId == studentId }
}