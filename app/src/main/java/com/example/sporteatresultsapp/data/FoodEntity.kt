package com.example.sporteatresultsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)
