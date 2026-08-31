package com.example.sporteatresultsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sporteatresultsapp.ui.theme.SportEatResultsAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val dataStoreManager = remember { DataStoreManager(context) }

            val mainViewModel: MainViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(dataStoreManager) as T
                    }
                }
            )

            val isDarkTheme by mainViewModel.isDarkTheme.collectAsState()
            val weightUnit by mainViewModel.weightUnit.collectAsState()
            val heightUnit by mainViewModel.heightUnit.collectAsState()

            SportEatResultsAppTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { mainViewModel.setTheme(it) },
                    weightUnit = weightUnit,
                    onWeightUnitChange = { mainViewModel.setWeightUnit(it) },
                    heightUnit = heightUnit,
                    onHeightUnitChange = { mainViewModel.setHeightUnit(it) }
                )
            }
        }
    }
}

