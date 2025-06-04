package com.example.lyndenflood_comp304_lab2_ex1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.resolveViewModel

class HabitViewModel : ViewModel() {

    //An internal mutable List that the only this ViewModel can modify
    private val _habits = MutableLiveData<List<Habit>>(emptyList());

    //A public immutable Live data that the UI and other components can observe
    val habits: LiveData<List<Habit>> = _habits

    // Function to add a new habit to the list
    fun addHabit(habit: Habit) {
        val currentHabits = _habits.value ?: emptyList()
        _habits.value = currentHabits + habit
    }



    //A function to update an existing habit in the list
    fun updateHabit(updatedHabit: Habit) {
        val currentHabits = _habits.value ?: emptyList()
        _habits.value = currentHabits.map { if (it.id == updatedHabit.id) updatedHabit else it }
    }

    // A function to delete a habit from the list
    fun deleteHabit(habit:Habit)
    {
        val currentHabits = _habits.value ?: emptyList();
        _habits.value = currentHabits.filter { it.id != habit.id }

    }

    // A function to clear all habits
    fun clearHabits() {
        _habits.value = emptyList()
    }

    // A function to get a habit by its ID
    fun getHabitById(id: Int): Habit? {
        return _habits.value?.find { it.id == id }
    }

}