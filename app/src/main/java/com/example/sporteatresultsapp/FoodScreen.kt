package com.example.sporteatresultsapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable


@Serializable
data class FoodItem(
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
) {
    val calories: Double
        get() = (protein * 4) + (fat * 9) + (carbs * 4)
}

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodViewModel = viewModel() // viewModel() сам создаст и запомнит VM
) {
    // состояния для полей ввода
    var foodName by remember { mutableStateOf("") }
    var proteinInput by remember { mutableStateOf("") }
    var fatInput by remember { mutableStateOf("") }
    var carbsInput by remember { mutableStateOf("") }

    // список продуктов, подписываемся на Flow из VM — список теперь из DataStore
    val foodList by viewModel.foodList.collectAsState()

    // итоговые значения
    val totalProtein = foodList.sumOf { it.protein }
    val totalFat = foodList.sumOf { it.fat }
    val totalCarbs = foodList.sumOf { it.carbs }
    val totalCalories = foodList.sumOf { it.calories }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Food Diary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddFoodForm(
            foodName = foodName,
            onFoodNameChange = { foodName = it },
            proteinInput = proteinInput,
            onProteinChange = { proteinInput = it },
            fatInput = fatInput,
            onFatChange = { fatInput = it },
            carbsInput = carbsInput,
            onCarbsChange = { carbsInput = it },
            onAddClick = {
                val protein = proteinInput.toDoubleOrNull() ?: 0.0
                val fat = fatInput.toDoubleOrNull() ?: 0.0
                val carbs = carbsInput.toDoubleOrNull() ?: 0.0

                if (foodName.isNotBlank() && (protein > 0 || fat > 0 || carbs > 0)) {
                    viewModel.addFood(FoodItem(foodName, protein, fat, carbs))
                    // очищаем поля
                    foodName = ""
                    proteinInput = ""
                    fatInput = ""
                    carbsInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TotalValuesCard(
            protein = totalProtein,
            fat = totalFat,
            carbs = totalCarbs,
            calories = totalCalories
        )

        Spacer(modifier = Modifier.height(16.dp))

        // список продуктов
        FoodListCard(
            foodList = foodList,
            onDeleteItem = { viewModel.deleteFood(it) },
            onClearAll = { viewModel.clearAll() }
        )
    }
}

@Composable
fun AddFoodForm(
    foodName: String,
    onFoodNameChange: (String) -> Unit,
    proteinInput: String,
    onProteinChange: (String) -> Unit,
    fatInput: String,
    onFatChange: (String) -> Unit,
    carbsInput: String,
    onCarbsChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Добавить продукт",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Название продукта
            OutlinedTextField(
                value = foodName,
                onValueChange = onFoodNameChange,
                label = { Text("Название продукта") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // поля для бжу
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = proteinInput,
                    onValueChange = onProteinChange,
                    label = { Text("Protein\n(g)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFFE91E63),
                        unfocusedLabelColor = Color(0xFFE91E63)
                    ),
                )

                OutlinedTextField(
                    value = fatInput,
                    onValueChange = onFatChange,
                    label = { Text("Fat\n(g)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF9C27B0),
                        unfocusedLabelColor = Color(0xFF9C27B0)
                    )
                )

                OutlinedTextField(
                    value = carbsInput,
                    onValueChange = onCarbsChange,
                    label = { Text("Carbs\n(g)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF673AB7),
                        unfocusedLabelColor = Color(0xFF673AB7)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Добавить", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TotalValuesCard(
    protein: Double,
    fat: Double,
    carbs: Double,
    calories: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Итого за день",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TotalValueItem(
                    label = "Calories",
                    value = String.format("%.1f", calories),
                    unit = "ккал",
                    color = Color(0xFFE91E63)
                )
                TotalValueItem(
                    label = "Protein",
                    value = String.format("%.1f", protein),
                    unit = "g",
                    color = Color(0xFF9C27B0)
                )
                TotalValueItem(
                    label = "Fat",
                    value = String.format("%.1f", fat),
                    unit = "g",
                    color = Color(0xFF673AB7)
                )
                TotalValueItem(
                    label = "Carbs",
                    value = String.format("%.1f", carbs),
                    unit = "g",
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun TotalValueItem(
    label: String,
    value: String,
    unit: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Text(
            text = unit,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FoodListCard(
    foodList: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Список продуктов",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${foodList.size} шт.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onClearAll,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.8f)
                )
            ) {
                Text("Clear all")
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )

            if (foodList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пока ничего не добавлено\nДобавьте свой первый продукт!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(foodList) { item ->
                        FoodListItem(
                            item = item,
                            onDelete = { onDeleteItem(item) }
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodListItem(
    item: FoodItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Б: ${String.format("%.1f", item.protein)}г",
                    fontSize = 13.sp,
                    color = Color(0xFFE91E63)
                )
                Text(
                    text = "Ж: ${String.format("%.1f", item.fat)}г",
                    fontSize = 13.sp,
                    color = Color(0xFF9C27B0)
                )
                Text(
                    text = "У: ${String.format("%.1f", item.carbs)}г",
                    fontSize = 13.sp,
                    color = Color(0xFF673AB7)
                )
                Text(
                    text = "${String.format("%.0f", item.calories)} ккал",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        }

        Button(
            onClick = onDelete,
            modifier = Modifier
                .width(60.dp)
                .height(36.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "X",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

