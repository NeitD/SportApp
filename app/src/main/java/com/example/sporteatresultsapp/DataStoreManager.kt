package com.example.sporteatresultsapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

// Расширение — создаёт DataStore один раз на весь app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "food_data")

class DataStoreManager(private val context: Context) {

    // key
    companion object {
        val FOOD_LIST_KEY = stringPreferencesKey("food_list")
    }

    // Сохранить список — suspend потому что это асинхронная операция
    suspend fun saveFoodList(foodList: List<FoodItem>) {
        val json = Json.encodeToString(foodList)
        context.dataStore.edit { preferences ->
            preferences[FOOD_LIST_KEY] = json
        }
    }

    // Читать список — возвращает Flow, UI сам обновится при изменении
    fun getFoodList(): Flow<List<FoodItem>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[FOOD_LIST_KEY] ?: return@map emptyList()
            Json.decodeFromString<List<FoodItem>>(json)
        }
    }
}