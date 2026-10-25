package com.example.sporteatresultsapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val WEIGHT_UNIT_KEY = stringPreferencesKey("weight_unit")
        val HEIGHT_UNIT_KEY = stringPreferencesKey("height_unit")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = isDark }
    }

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DARK_THEME_KEY] ?: false }

    suspend fun saveWeightUnit(unit: String) {
        context.dataStore.edit { it[WEIGHT_UNIT_KEY] = unit }
    }

    val weightUnitFlow: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[WEIGHT_UNIT_KEY] ?: "kg" }

    suspend fun saveHeightUnit(unit: String) {
        context.dataStore.edit { it[HEIGHT_UNIT_KEY] = unit }
    }

    val heightUnitFlow: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[HEIGHT_UNIT_KEY] ?: "cm" }

    suspend fun saveLoggedInState(isLoggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN_KEY] = isLoggedIn }
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[IS_LOGGED_IN_KEY] ?: false }
}
