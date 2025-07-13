package com.example.lyndenflood_comp304lab3_ex1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lyndenflood_comp304lab3_ex1.model.FitnessActivity

@Database(entities = [FitnessActivity::class], version = 1)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun activityDao(): FitnessActivityDao

    companion object {
        @Volatile private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
