package com.example.sporteatresultsapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    weightUnit: String,
    onWeightUnitChange: (String) -> Unit,
    heightUnit: String,
    onHeightUnitChange: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // верхняя панель + кнопка возврата
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Настройки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SettingItem(
                    title = "Notifications",
                    initialValue = true,
                    onCheckedChange = {}
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SettingItem(
                    title = "Dark theme",
                    initialValue = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SettingClickableItem(
                    title = "Weight unit",
                    value = if (weightUnit == "kg") "Kilograms (kg)" else "Pounds (lbs)",
                    onClick = {
                        val nextUnit = if (weightUnit == "kg") "lbs" else "kg"
                        onWeightUnitChange(nextUnit)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                SettingClickableItem(
                    title = "Height unit",
                    value = if (heightUnit == "cm") "Centimeters (cm)" else "Inches (in)",
                    onClick = {
                        val nextUnit = if (heightUnit == "cm") "in" else "cm"
                        onHeightUnitChange(nextUnit)
                    }
                )

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Log out")
                }
            }
        }
    }
}

@Composable
fun SettingItem(title: String, initialValue: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp)
        Switch(checked = initialValue, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingClickableItem(title: String, value: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
        Text(text = value, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
    }
}