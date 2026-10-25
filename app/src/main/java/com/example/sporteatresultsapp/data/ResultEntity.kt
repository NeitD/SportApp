package com.example.sporteatresultsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseName: String,
    val weight: Double
)
