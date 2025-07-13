package com.example.lyndenflood_comp304lab3_ex1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lyndenflood_comp304lab3_ex1.model.FitnessActivity

@Dao
interface FitnessActivityDao {
    @Insert suspend fun insert(activity: FitnessActivity)

    @Query("SELECT * FROM fitness_activity ORDER BY date DESC")
    suspend fun getAll(): List<FitnessActivity>

    @Delete suspend fun delete(activity: FitnessActivity)

    @Update
    suspend fun update(activity: FitnessActivity)


    @Query("SELECT * FROM fitness_activity WHERE id = :id")
    suspend fun getById(id: Int): FitnessActivity?
}
