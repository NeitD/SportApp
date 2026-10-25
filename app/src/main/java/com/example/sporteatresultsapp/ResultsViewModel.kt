package com.example.sporteatresultsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporteatresultsapp.data.AppDatabase
import com.example.sporteatresultsapp.data.ResultEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val resultsDao = AppDatabase.getDatabase(application).resultsDao()

    val resultsList: StateFlow<List<ResultItem>> = resultsDao
        .getAllResults()
        .map { entities ->
            entities.map { entity ->
                ResultItem(
                    id = entity.id,
                    exerciseName = entity.exerciseName,
                    weight = entity.weight
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addResult(item: ResultItem) {
        viewModelScope.launch(Dispatchers.IO) {
            resultsDao.insertResult(
                ResultEntity(
                    id = item.id,
                    exerciseName = item.exerciseName,
                    weight = item.weight
                )
            )
        }
    }

    fun deleteResult(item: ResultItem) {
        viewModelScope.launch(Dispatchers.IO) {
            resultsDao.deleteResult(
                ResultEntity(
                    id = item.id,
                    exerciseName = item.exerciseName,
                    weight = item.weight
                )
            )
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            resultsDao.clearAllResults()
        }
    }
}
