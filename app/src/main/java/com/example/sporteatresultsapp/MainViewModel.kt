package com.example.sporteatresultsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    val isDarkTheme = dataStoreManager.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val weightUnit = dataStoreManager.weightUnitFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "kg")

    val heightUnit = dataStoreManager.heightUnitFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "cm")

    val isLoggedIn = dataStoreManager.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveTheme(isDark)
        }
    }

    fun setWeightUnit(unit: String) {
        viewModelScope.launch {
            dataStoreManager.saveWeightUnit(unit)
        }
    }

    fun setHeightUnit(unit: String) {
        viewModelScope.launch {
            dataStoreManager.saveHeightUnit(unit)
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveLoggedInState(isLoggedIn)
        }
    }
}
