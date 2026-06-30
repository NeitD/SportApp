package com.example.sporteatresultsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    val resultsList: StateFlow<List<ResultItem>> = dataStoreManager
        .getResultsList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addResult(item: ResultItem) {
        viewModelScope.launch {
            val updated = resultsList.value + item
            dataStoreManager.saveResultsList(updated)
        }
    }

    fun deleteResult(item: ResultItem) {
        viewModelScope.launch {
            val updated = resultsList.value - item
            dataStoreManager.saveResultsList(updated)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            dataStoreManager.saveResultsList(emptyList())
        }
    }
}