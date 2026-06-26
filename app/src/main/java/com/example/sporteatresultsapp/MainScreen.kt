package com.example.sporteatresultsapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporteatresultsapp.ui.theme.SportEatResultsAppTheme

data class NavItem(
    val label: String,
    val iconSelected: Int,
    val iconUnselected: Int,
)

// Модель данных для упражнения
data class Exercise(
    val number: String,
    val name: String,
    val setsAndReps: String,
    val notes: String = ""
)

// Модель для тренировочного дня
data class WorkoutDay(
    val name: String,
    val exercises: List<Exercise>
)

// Модель для программы с периодизацией
data class WorkoutProgram(
    val name: String,
    val days: List<WorkoutDay>
)

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

   var selectedIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        NavItem("Программа", R.drawable.gym, R.drawable.gym),
        NavItem("КБЖУ", R.drawable.food, R.drawable.food),
        NavItem("Результаты", R.drawable.crown, R.drawable.crown),
        NavItem("Профиль", R.drawable.baseline_person_24, R.drawable.baseline_person_outline_24)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (selectedIndex == index) item.iconSelected else item.iconUnselected),
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedIndex) {
            0 -> ProgramScreen(modifier = Modifier.padding(paddingValues))
            1 -> FoodScreen(modifier = Modifier.padding(paddingValues))
            2 -> ResultsScreen(modifier = Modifier.padding(paddingValues))
            3 -> ProfileScreen(modifier = Modifier.padding(paddingValues))
        }
    }

}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview(modifier: Modifier = Modifier) {
    SportEatResultsAppTheme() {
        MainScreen()
    }
}

@Composable
fun ResultsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("результаты", fontSize = 24.sp)
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("профиль", fontSize = 24.sp)
    }
}



