package com.example.sporteatresultsapp

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    // profile data (hard)
    var firstName by remember { mutableStateOf("Damon") }
    var lastName by remember { mutableStateOf("Neit") }
    var weight by remember { mutableStateOf("80") }
    var height by remember { mutableStateOf("177") }
    var birthDate by remember { mutableStateOf(LocalDate.of(2006, 10, 10)) }

    var age = remember(birthDate) {
        Period.between(birthDate, LocalDate.now()).years
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // calories
    var targetCalories by remember { mutableStateOf("3020") }
    var targetProtein by remember { mutableStateOf("165") }
    var targetFat by remember { mutableStateOf("60") }
    var targetCarbs by remember { mutableStateOf("460") }

    val context = LocalContext.current

    val datePickerDialog = remember(birthDate) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                birthDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            birthDate.year,
            birthDate.monthValue - 1, // так как datepicker использует 0-based месяцы
            birthDate.dayOfMonth
        )
    }

    // BMI
    val weightDouble = weight.toDoubleOrNull() ?: 0.0
    val heightDouble = height.toDoubleOrNull() ?: 1.0 // чтобы на ноль не делить

    val bmi = remember(weightDouble, heightDouble) {
        if (heightDouble > 0) weightDouble / ((heightDouble / 100) * (heightDouble / 100)) else 0.0
    }

    val bmiStatus = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal weight"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }

    val bmiColor = when {
        bmi < 18.5 -> Color(0xFF03A9F4)
        bmi < 25 -> Color(0xFF4CAF50)
        bmi < 30 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileInfoColumn(
                firstName = firstName,
                lastName = lastName,
                weight = weight,
                height = height,
                birthDate = birthDate.format(dateFormatter),
                age = age,
                onBirthDateClick = { datePickerDialog.show() },
                modifier = Modifier.weight(1f)
            )

            DailyTargetsColumn(
                calories = targetCalories,
                protein = targetProtein,
                fat = targetFat,
                carbs = targetCarbs,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BmiCard(
                bmi = bmi,
                status = bmiStatus,
                color = bmiColor,
                modifier = Modifier.weight(1f)
            )

            WaterTrackerCard(
                weight = weightDouble,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// first block
@Composable
fun ProfileInfoColumn(
    firstName: String,
    lastName: String,
    weight: String,
    height: String,
    birthDate: String,
    age: Int,
    onBirthDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Profile info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            ProfileInfoRow(label = "First name", value = firstName)
            ProfileInfoRow(label = "Last name", value = lastName)
            ProfileInfoRow(label = "Weight", value = "$weight kg")
            ProfileInfoRow(label = "Height", value = "$height cm")

            Column(
                modifier = Modifier.clickable(onClick = onBirthDateClick)
            ) {
                Text(
                    text = "Date of birth",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = birthDate,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$age years old",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String
) {
Column {
    Text(
        text = label,
        fontSize = 12.sp,
        color = Color.Gray
    )
    Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}
}

// second block
@Composable
fun DailyTargetsColumn(
    calories: String,
    protein: String,
    fat: String,
    carbs: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Daily target",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            TargetRow(label = "Calories", value = "$calories kcal", color = Color(0xFFE91E63))
            TargetRow(label = "Protein", value = "$protein g", color = Color(0xFF9C27B0))
            TargetRow(label = "Fat", value = "$fat g", color = Color(0xFF673AB7))
            TargetRow(label = "Carbs", value = "$carbs g", color = Color(0xFF2196F3))
        }
    }
}

@Composable
fun TargetRow(
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun BmiCard(
    bmi: Double,
    status: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Body Mass Index (BMI)",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "%.1f".format(bmi),
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )

            Text(
                text = status,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = "Based on your weight and height",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WaterTrackerCard(
    weight: Double,
    modifier: Modifier = Modifier
) {
    // Расчет: 33 мл на 1 кг веса
    val waterGoal = (weight * 0.033).let { "%.1f".format(it) }

    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Water Intake",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$waterGoal L",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "Daily goal",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "Hydration helps your metabolism and energy!",
                fontSize = 12.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}