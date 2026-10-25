package com.example.sporteatresultsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table ORDER BY id DESC")
    fun getAllFood(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: FoodEntity)

    @Delete
    fun deleteFood(food: FoodEntity)

    @Query("DELETE FROM food_table")
    fun clearAllFood()
}
