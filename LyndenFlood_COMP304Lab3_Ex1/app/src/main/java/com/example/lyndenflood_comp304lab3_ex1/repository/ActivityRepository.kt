package com.example.lyndenflood_comp304lab3_ex1.repository

import android.util.Log
import com.example.lyndenflood_comp304lab3_ex1.data.FitnessActivityDao
import com.example.lyndenflood_comp304lab3_ex1.model.FitnessActivity

class ActivityRepository(private val dao: FitnessActivityDao) {
    suspend fun insert(activity: FitnessActivity) = dao.insert(activity)
    suspend fun getAll(): List<FitnessActivity> {
        val result = dao.getAll()
        Log.d("ActivityRepository", "📦 DAO returned ${result.size} activities")
        return result
    }
    suspend fun delete(activity: FitnessActivity) = dao.delete(activity)
    suspend fun getById(id: Int): FitnessActivity? = dao.getById(id)
    suspend fun update(activity: FitnessActivity) = dao.update(activity)

}
