package com.example.sporteatresultsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ProgramScreen(modifier: Modifier = Modifier) {
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    val workoutProgram = getSampleWorkoutProgram()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Training program",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        DaySelector(
            days = workoutProgram.days.map { it.name },
            selectedIndex = selectedDayIndex,
            onDaySelected = { selectedDayIndex = it }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            ExerciseTable(exercises = workoutProgram.days[selectedDayIndex].exercises)
        }

        RestTimerCard()
    }
}

@Composable
fun DaySelector(
    days: List<String>,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            days.forEachIndexed { index, day ->
                SegmentedButton(
                    selected = selectedIndex == index,
                    onClick = { onDaySelected(index) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = days.size
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                        fontSize = 13.sp,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseTable(exercises: List<Exercise>) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TableHeader()

            HorizontalDivider(Modifier, thickness = 1.dp, color = Color.Gray)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(exercises) { exercise ->
                    TableRow(exercise = exercise)
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "№",
            modifier = Modifier.weight(0.2f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "Упражнение",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "Подходы × Повторы",
            modifier = Modifier.weight(0.7f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun TableRow(exercise: Exercise) {
    // узнаём колво подходов
    val totalSets = exercise.setsAndReps.firstOrNull { it.isDigit() }?.digitToInt() ?: 3

    // память для галочек
    var completedSets by remember(exercise) {
        mutableStateOf(List(totalSets) { false })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.number,
                modifier = Modifier.weight(0.2f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = exercise.name,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = exercise.setsAndReps,
                modifier = Modifier.weight(0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }

        if (exercise.notes.isNotEmpty()) {
            Text(
                text = exercise.notes,
                modifier = Modifier.padding(start = 0.dp, top = 4.dp),
                fontSize = 12.sp,
                color = Color.Gray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        // ряд с подходами
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Подходы:",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )

            for (i in 0 until totalSets) {
                val isChecked = completedSets.getOrElse(i) { false }

                FilterChip(
                    selected = isChecked,
                    onClick = {
                        val newList = completedSets.toMutableList()
                        newList[i] = !newList[i]
                        completedSets = newList
                    },
                    label = { Text("Set ${i + 1}", fontSize = 11.sp)},
                    leadingIcon = if (isChecked) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    } else null
                )
            }
        }
    }
}

@Composable
fun RestTimerCard() {
    var selectedMinutes by remember { mutableIntStateOf(1) }
    var selectedSeconds by remember { mutableIntStateOf(30) }

    var totalTimeSeconds by remember { mutableIntStateOf(90) }
    var timeRemainingSeconds by remember { mutableIntStateOf(90) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Асинхронный отсчет времени каждой секунды
    LaunchedEffect(isTimerRunning, timeRemainingSeconds) {
        if (isTimerRunning && timeRemainingSeconds > 0) {
            delay(1000L)
            timeRemainingSeconds -= 1
        } else if (timeRemainingSeconds == 0) {
            isTimerRunning = false
        }
    }

    val minutesList = (0..15).toList()
    val secondsList = (0..59).toList()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⏱️ Таймер отдыха",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isTimerRunning && timeRemainingSeconds == totalTimeSeconds) {
                // Выбор времени колесиками минут и секунд
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Колесо минут
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Мин", fontSize = 12.sp, color = Color.Gray)
                        WheelPicker(
                            items = minutesList,
                            initialValue = selectedMinutes,
                            onItemSelected = {
                                selectedMinutes = it
                                totalTimeSeconds = selectedMinutes * 60 + selectedSeconds
                                timeRemainingSeconds = totalTimeSeconds
                            }
                        )
                    }

                    Text(
                        text = ":",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // Колесо секунд
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Сек", fontSize = 12.sp, color = Color.Gray)
                        WheelPicker(
                            items = secondsList,
                            initialValue = selectedSeconds,
                            onItemSelected = {
                                selectedSeconds = it
                                totalTimeSeconds = selectedMinutes * 60 + selectedSeconds
                                timeRemainingSeconds = totalTimeSeconds
                            }
                        )
                    }
                }
            } else {
                // Отображение большого таймера обратного отсчета
                val currentMin = timeRemainingSeconds / 60
                val currentSec = timeRemainingSeconds % 60
                val formattedTime = String.format("%02d:%02d", currentMin, currentSec)

                Text(
                    text = formattedTime,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (timeRemainingSeconds <= 5 && timeRemainingSeconds > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )

                if (timeRemainingSeconds == 0) {
                    Text(
                        text = "Отдых окончен! Вперёд к подходу! 💪",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Кнопки Управления (Старт / Пауза / Сброс)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (timeRemainingSeconds == 0) {
                            timeRemainingSeconds = totalTimeSeconds
                        }
                        isTimerRunning = !isTimerRunning
                    },
                    enabled = totalTimeSeconds > 0
                ) {
                    Icon(
                        imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isTimerRunning) "Пауза" else "Старт")
                }

                OutlinedButton(
                    onClick = {
                        isTimerRunning = false
                        timeRemainingSeconds = totalTimeSeconds
                    }
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Сброс")
                }
            }
        }
    }
}

@Composable
fun WheelPicker(
    items: List<Int>,
    initialValue: Int, // Начальное выбранное значение
    onItemSelected: (Int) -> Unit // Колбэк при выборе числа
) {
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = items.indexOf(initialValue))
    // поведение магнита при скролле
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // отслеживаем остановку скролла
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex
            if (centerIndex in items.indices) {
                onItemSelected(items[centerIndex])
            }
        }
    }

    Box(
        modifier = Modifier
            .height(90.dp)
            .width(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // подсветка активной строчки в центре колеса
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            shape = RoundedCornerShape(8.dp)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(30.dp)) } // отступ
            itemsIndexed(items) { index, item ->
                val isSelected = listState.firstVisibleItemIndex == index
                Text(
                    text = String.format("%02d", item),
                    fontSize = if (isSelected) 20.sp else 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(30.dp)) } // отступ
        }
    }
}

fun getSampleWorkoutProgram(): WorkoutProgram {
    return WorkoutProgram(
        name = "Тяжёлая неделя",
        days = listOf(
            // Upper 1
            WorkoutDay(
                name = "UPPER 1",
                exercises = listOf(
                    Exercise("1", "Жим гантелей на наклонной", "4×6–8 → 8–10 → 12-15", "RIR 1"),
                    Exercise("2", "Жим штанги лёжа (силовой)", "3×4–6", "RIR 1–2"),
                    Exercise("3", "Подтягивания параллельным хватом", "4×6–8", "RIR 1"),
                    Exercise("4", "Тяга горизонтального блока узким хватом", "4×8–12", ""),
                    Exercise(
                        "5",
                        "Разведения на среднюю дельту (кабель одноручный)",
                        "3×12–15",
                        ""
                    ),
                    Exercise("6", "Разведения на заднюю дельту (обратная PEC-Deck)", "3×12–15", ""),
                    Exercise("7", "Трицепс: жим на брусьях (вверх неполный)", "3×6–10", ""),
                    Exercise("8", "Трицепс: разгибание каната сверху", "3×12–15", ""),
                    Exercise("9", "Гиперэкстензия (очень лёгкая)", "2×15–20", ""),
                    Exercise("10", "Сгибания кисти + обратные сгибания", "2×15 + 2×15", "")
                )
            ),
            // LOWER
            WorkoutDay(
                name = "LOWER",
                exercises = listOf(
                    Exercise("1", "Разгибания ног", "2×12–15", ""),
                    Exercise("2", "Сгибания ног лёжа / сидя", "2×10–12", ""),
                    Exercise("3", "Разведение ног", "2×10–12", ""),
                    Exercise("4", "Сведение ног", "2×10–12", ""),
                    Exercise("5", "Гак присед", "4×10–15", ""),
                    Exercise("6", "Жим одной ногой", "3×8-12 на ногу", ""),
                    Exercise("7", "Отведение ноги назад в тренажёре на жопу", "3×8-12", ""),
                    Exercise("8", "Подъёмы на носки (икры)", "3×12–20", ""),
                    Exercise("9", "Пресс: подъёмы ног в висе", "3×12–15", ""),
                    Exercise("10", "Скручивания (верх пресса)", "3×12–20", ""),
                    Exercise("11", "Косые мышцы: тяга каната стоя", "3×12–15 на сторону", ""),
                    Exercise("12", "Планка", "2×60 секунд", ""),
                    Exercise("13", "Лёгкий бег/ходьба в гору", "5 минут", "Опционально")
                )
            ),
            // UPPER 2
            WorkoutDay(
                name = "UPPER 2",
                exercises = listOf(
                    Exercise("1", "Жим гантелей лёжа", "4×8–12", ""),
                    Exercise("2", "Сведение в кроссовере", "3×12–15", ""),
                    Exercise("3", "Подтягивания широким хватом", "4×6–10", ""),
                    Exercise("4", "Тяга \"гребля\" в тренажёре", "3×8–12", ""),
                    Exercise("5", "Средняя дельта: махи в тренажёре", "3×12–15", ""),
                    Exercise("5+", "Шраги с гантелями / в Смите", "3×10–12", ""),
                    Exercise("6", "Задняя дельта: кабель крест-обратная тяга", "2×12–15", ""),
                    Exercise("7", "Жим гантелей сидя (лёгкий, контрольный)", "2×8–10", ""),
                    Exercise("8", "Бицепс: молотковые", "3×8–10", ""),
                    Exercise("9", "Бицепс: скамья Скотта", "3×12–15", ""),
                    Exercise(
                        "10",
                        "Обратные сгибания EZ",
                        "2×12",
                        "Только на тяжёлой и средней неделе"
                    ),
                    Exercise("", "Вакуум + Планка", "Отдельно в четверг", "")
                )
            )
        )
    )
}
