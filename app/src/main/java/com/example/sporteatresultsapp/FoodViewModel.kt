package com.example.sporteatresultsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodViewModel(application: Application): AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    // StateFlow — как Flow, но всегда хранит последнее значение
    // UI подписывается на него и автоматически перерисовывается
    val foodList: StateFlow<List<FoodItem>> = dataStoreManager
        .getFoodList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFood(item: FoodItem) {
        viewModelScope.launch { // запускаем корутину для async операции
            val updated = foodList.value + item
            dataStoreManager.saveFoodList(updated)
        }
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch {
            val updated = foodList.value - item
            dataStoreManager.saveFoodList(updated)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            dataStoreManager.saveFoodList(emptyList())
        }
    }
}