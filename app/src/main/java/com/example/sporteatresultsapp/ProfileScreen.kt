package com.example.sporteatresultsapp

import android.view.RoundedCorner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

// first block
@Composable
fun ProfileInfoColumn(modifier: Modifier = Modifier) {

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
        modifier = Modifier.fillMaxHeight(),
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

            TargetRow(label = "Calories", value = "$calories cal", color = Color(0xFFE91E63))
            TargetRow(label = "Protein", value = "$protein cal", color = Color(0xFF9C27B0))
            TargetRow(label = "Fat", value = "$fat cal", color = Color(0xFF673AB7))
            TargetRow(label = "Carbs", value = "$carbs cal", color = Color(0xFF2196F3))
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