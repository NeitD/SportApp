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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
data class ResultItem(
   val exerciseName: String,
    val weight: Double
)

@Composable
fun ResultsScreen(
    modifier: Modifier = Modifier,
    viewModel: ResultsViewModel = viewModel()
) {
    var exerciseName by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }

    val resultsList by viewModel.resultsList.collectAsState()

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Results",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddResultForm(
            exerciseName = exerciseName,
            onExerciseNameChange = { exerciseName = it },
            weightInput = weightInput,
            onWeightChange = { weightInput = it },
            onAddClick = {
                val weight = weightInput.toDoubleOrNull() ?: 0.0

                if (exerciseName.isNotBlank() && weight > 0) {
                    viewModel.addResult(ResultItem(exerciseName, weight))
                    exerciseName = ""
                    weightInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResultsListCard(
            resultsList = resultsList,
            onDeleteItem = { viewModel.deleteResult(it) },
            onClearAll = { viewModel.clearAll() }
        )
    }
}

@Composable
fun AddResultForm(
    exerciseName: String,
    onExerciseNameChange: (String) -> Unit,
    weightInput: String,
    onWeightChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "New record",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = exerciseName,
                onValueChange = onExerciseNameChange,
                label = { Text("Exercise name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = weightInput,
                onValueChange = onWeightChange,
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ResultsListCard(
    resultsList: List<ResultItem>,
    onDeleteItem: (ResultItem) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    text = "List of records",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${resultsList.size} units.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )

            if (resultsList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пока ничего не добавлено\nЗапишите свой первый рекорд!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(resultsList) { item ->
                        ResultListItem(
                            item = item,
                            onDelete = { onDeleteItem(item) }
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }

                Button(
                    onClick = onClearAll,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Clear all")
                }
            }
        }
    }
}

@Composable
fun ResultListItem(
    item: ResultItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.exerciseName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${String.format("%.1f", item.weight)} kg",
                fontSize = 14.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onDelete,
            modifier = Modifier
                .width(60.dp)
                .height(36.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("X", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}