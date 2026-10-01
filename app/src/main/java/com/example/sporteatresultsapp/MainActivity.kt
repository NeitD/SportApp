package com.example.sporteatresultsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sporteatresultsapp.ui.theme.SportEatResultsAppTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

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
            mainViewModel.isLoggedIn.value == null
        }

        enableEdgeToEdge()

        setContent {
            val isDarkTheme by mainViewModel.isDarkTheme.collectAsState()
            val weightUnit by mainViewModel.weightUnit.collectAsState()
            val heightUnit by mainViewModel.heightUnit.collectAsState()
            val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()

            SportEatResultsAppTheme(darkTheme = isDarkTheme) {
                when (isLoggedIn) {
                    null -> {
                        Box(modifier = Modifier.fillMaxSize())
                    }
                    true -> {
                        MainScreen(
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { mainViewModel.setTheme(it) },
                            weightUnit = weightUnit,
                            onWeightUnitChange = { mainViewModel.setWeightUnit(it) },
                            heightUnit = heightUnit,
                            onHeightUnitChange = { mainViewModel.setHeightUnit(it) },
                            onLogout = { mainViewModel.setLoggedIn(false) }
                        )
                    }
                    false -> {
                        LoginScreen(
                            onLoginSuccess = { mainViewModel.setLoggedIn(true) }
                        )
                    }
                }
            }
        }
    }
}

