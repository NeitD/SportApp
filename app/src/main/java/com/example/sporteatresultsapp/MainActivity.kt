package com.example.sporteatresultsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sporteatresultsapp.ui.theme.SportEatResultsAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = this
        val dataStoreManager = DataStoreManager(context)

        val mainViewModel: MainViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(dataStoreManager) as T
                }
            }
        )[MainViewModel::class.java]

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value is MainUiState.Loading
        }

        setContent {
            val uiState by mainViewModel.uiState.collectAsState()

            when (val state = uiState) {
                is MainUiState.Loading -> {
                    // Пока данные загружаются из DataStore, заставка от SplashScreen удерживает экран
                }
                is MainUiState.Success -> {
                    SportEatResultsAppTheme(darkTheme = state.isDarkTheme) {
                        if (state.isLoggedIn) {
                            MainScreen(
                                isDarkTheme = state.isDarkTheme,
                                onThemeChange = { mainViewModel.setTheme(it) },
                                weightUnit = state.weightUnit,
                                onWeightUnitChange = { mainViewModel.setWeightUnit(it) },
                                heightUnit = state.heightUnit,
                                onHeightUnitChange = { mainViewModel.setHeightUnit(it) },
                                onLogout = { mainViewModel.setLoggedIn(false) }
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = { mainViewModel.setLoggedIn(true) }
                            )
                        }
                    }
                }
            }
        }
    }
}
