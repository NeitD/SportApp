package com.example.sporteatresultsapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    var isDarkTheme = dataStoreManager.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveTheme(isDark)
        }
    }
}
