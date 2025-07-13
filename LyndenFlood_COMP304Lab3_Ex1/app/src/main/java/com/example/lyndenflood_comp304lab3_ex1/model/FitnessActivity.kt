package com.example.lyndenflood_comp304lab3_ex1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness_activity")
data class FitnessActivity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val durationMinutes: Int,
    val date: String // Format: YYYY-MM-DD
)
