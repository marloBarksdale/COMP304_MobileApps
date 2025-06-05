package com.example.lyndenflood_comp304_lab2_ex1

import java.time.LocalDate

enum class Frequency{
    DAILY,
    WEEKLY,
    MONTHLY
}

data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: LocalDate,
    val frequency: Frequency?,
    val goal: Int,
    val completedDates: List<LocalDate>
)
