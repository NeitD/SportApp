package com.example.sporteatresultsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporteatresultsapp.data.AppDatabase
import com.example.sporteatresultsapp.data.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val foodDao = AppDatabase.getDatabase(application).foodDao()

    val foodList: StateFlow<List<FoodItem>> = foodDao
        .getAllFood()
        .map { entities ->
            entities.map { entity ->
                FoodItem(
                    id = entity.id,
                    name = entity.name,
                    protein = entity.protein,
                    fat = entity.fat,
                    carbs = entity.carbs
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFood(item: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            foodDao.insertFood(
                FoodEntity(
                    id = item.id,
                    name = item.name,
                    protein = item.protein,
                    fat = item.fat,
                    carbs = item.carbs
                )
            )
        }
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            foodDao.deleteFood(
                FoodEntity(
                    id = item.id,
                    name = item.name,
                    protein = item.protein,
                    fat = item.fat,
                    carbs = item.carbs
                )
            )
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            foodDao.clearAllFood()
        }
    }
}
