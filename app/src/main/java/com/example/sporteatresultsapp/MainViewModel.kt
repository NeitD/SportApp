package com.example.sporteatresultsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val isDarkTheme: Boolean,
        val weightUnit: String,
        val heightUnit: String,
        val isLoggedIn: Boolean
    ) : MainUiState
}

class MainViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        dataStoreManager.isDarkThemeFlow,
        dataStoreManager.weightUnitFlow,
        dataStoreManager.heightUnitFlow,
        dataStoreManager.isLoggedInFlow
    ) { isDark, weightUnit, heightUnit, isLoggedIn ->
        MainUiState.Success(
            isDarkTheme = isDark,
            weightUnit = weightUnit,
            heightUnit = heightUnit,
            isLoggedIn = isLoggedIn
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState.Loading
    )

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
