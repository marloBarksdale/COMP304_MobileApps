package com.example.lyndenflood_comp304lab3_ex1.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyndenflood_comp304lab3_ex1.data.FitnessDatabase
import com.example.lyndenflood_comp304lab3_ex1.model.FitnessActivity
import com.example.lyndenflood_comp304lab3_ex1.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActivityViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = FitnessDatabase.getDatabase(app).activityDao()
    private val repository = ActivityRepository(dao)

    private val _activities = MutableStateFlow<List<FitnessActivity>>(emptyList())
    val activities: StateFlow<List<FitnessActivity>> = _activities

    init {
        loadActivities()
    }

    private fun loadActivities() = viewModelScope.launch {
        val list = repository.getAll().map { it.copy() }
        Log.d("ActivityViewModel", "🔁 loadActivities: ${list.size} items")
        _activities.value = list
    }

    fun addActivity(type: String, duration: Int, date: String) = viewModelScope.launch {
        repository.insert(FitnessActivity(type = type, durationMinutes = duration, date = date))
        loadActivities()
    }

    fun getActivityById(id: Int, onResult: (FitnessActivity?) -> Unit) = viewModelScope.launch {
        val activity = repository.getById(id)
        onResult(activity)
    }

    fun updateActivity(activity: FitnessActivity) = viewModelScope.launch {
        repository.update(activity)
        loadActivities()
    }

    fun deleteActivity(activity: FitnessActivity, onComplete: () -> Unit) = viewModelScope.launch {

        repository.delete(activity)
        Log.d("ActivityViewModel", "🗑️ Deleting activity ${activity.id}")

        loadActivities()
        onComplete()
    }


}
