package com.example.sporteatresultsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultsDao {
    @Query("SELECT * FROM results_table ORDER BY id DESC")
    fun getAllResults(): Flow<List<ResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(result: ResultEntity)

    @Delete
    fun deleteResult(result: ResultEntity)

    @Query("DELETE FROM results_table")
    fun clearAllResults()
}
