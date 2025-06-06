package com.example.lyndenflood_comp304_lab2_ex1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HabitViewModel : ViewModel() {

    // Internal mutable state flow holding the list of habits
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())

    // Publicly exposed immutable state flow for observing habits
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    // Add a new habit to the list
    fun addHabit(habit: Habit) {
        _habits.value = _habits.value + habit
    }

    // Update an existing habit by matching its id
    fun updateHabit(updatedHabit: Habit) {
        _habits.value = _habits.value.map {
            if (it.id == updatedHabit.id) updatedHabit else it
        }
    }

    // Remove a habit from the list by id
    fun deleteHabit(habit: Habit) {
        _habits.value = _habits.value.filter { it.id != habit.id }
    }

    // Clear all habits from the list
    fun clearHabits() {
        _habits.value = emptyList()
    }

    // Retrieve a habit by its id, or null if not found
    fun getHabitById(id: Int): Habit? {
        return _habits.value.find { it.id == id }
    }

    // Calculate the current streak of consecutive days the habit was completed
    fun calculateStreak(habit: Habit): Int {
        val sortedDates = habit.completedDates.sortedDescending()
        var streak = 0
        var current = LocalDate.now()

        for (date in sortedDates) {
            if (date == current) {
                streak++
                current = current.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }

}