package com.example.fit_buddy.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.viewmodel.WorkoutViewModel
import kotlin.math.abs

/* ================== HELPER FUNCTIONS ================== */
fun calculateBMI(weightKg: Double, heightM: Double): Double {
    if (heightM <= 0) return 0.0
    return weightKg / (heightM * heightM)
}

fun estimateBodyFat(bmi: Double, gender: String, age: Int = 25): Double {
    val genderValue = if (gender.lowercase() == "male") 1 else 0
    return (1.20 * bmi) + (0.23 * age) - (10.8 * genderValue) - 5.4
}

fun estimateMuscleMass(weight: Double, bodyFat: Double): Double {
    return weight * (1 - bodyFat / 100)
}

fun formatChange(value: Double, unit: String): String {
    return when {
        value > 0 -> "↑ ${String.format("%.1f", value)} $unit"
        value < 0 -> "↓ ${String.format("%.1f", abs(value))} $unit"
        else -> "Stable"
    }
}

/* ================== MAIN SCREEN ================== */
@Composable
fun AchievementScreen() {
    val workoutViewModel: WorkoutViewModel = viewModel()
    val streakData by workoutViewModel.streakData.collectAsState(WorkoutViewModel.StreakData())
    val graphData by workoutViewModel.graphData.collectAsState(WorkoutViewModel.GraphData())

    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.observeAsState()

    LaunchedEffect(Unit) { userViewModel.loadCurrentUser() }

    Scaffold(containerColor = backgroundLightLavender) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(padding)
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Achievements", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(Modifier.height(24.dp))

            StreakCard(streakData = streakData)
            Spacer(Modifier.height(26.dp))

            GoalCard()
            Spacer(Modifier.height(26.dp))

            Text("Daily Workout Time (Last 30 Days)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(Modifier.height(16.dp))
            SimpleBarChart(minutesList = graphData.minutes)
            Spacer(Modifier.height(30.dp))

            Text("Body Metrics", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(Modifier.height(18.dp))
            BodyMetricsGrid(user, userViewModel)
            Spacer(Modifier.height(30.dp))

        }
    }
}

/* ================== STREAK CARD ================== */
@Composable
fun StreakCard(streakData: WorkoutViewModel.StreakData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(streakGradientStart, streakGradientEnd)))
            .padding(24.dp)
    ) {
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("${streakData.currentStreak}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
                    Text("Day Streak", fontSize = 14.sp, color = backgroundWhite.copy(0.9f))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${streakData.bestStreak}", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
                    Text("Best Streak", fontSize = 13.sp, color = backgroundWhite.copy(0.9f))
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                streakData.last7Days.forEach { (day, checked) -> DayCheck(day, checked) }
            }
        }
    }
}

@Composable
fun DayCheck(day: String, checked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(50)).background(backgroundWhite),
            contentAlignment = Alignment.Center
        ) { if (checked) Text("✓", color = rose500, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        Spacer(Modifier.height(6.dp))
        Text(day, fontSize = 11.sp, color = backgroundWhite)
    }
}

/* ================== BAR CHART ================== */
@Composable
fun SimpleBarChart(minutesList: List<Int>) {
    if (minutesList.isEmpty() || minutesList.all { it == 0 }) {
        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("No workout data yet", color = textSecondary)
        }
        return
    }

    val maxMinutes = minutesList.maxOrNull()?.coerceAtLeast(1) ?: 1
    val barWidth = 12.dp
    val spacing = 6.dp

    Canvas(Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 16.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barWidthPx = barWidth.toPx()
        val spacingPx = spacing.toPx()
        val totalBarsWidth = minutesList.size * barWidthPx + (minutesList.size - 1) * spacingPx
        val startX = (canvasWidth - totalBarsWidth) / 2

        minutesList.forEachIndexed { index, minutes ->
            val barHeight = (minutes.toFloat() / maxMinutes) * (canvasHeight * 0.8f)
            val x = startX + index * (barWidthPx + spacingPx)
            drawRoundRect(color = lavender500, topLeft = Offset(x, canvasHeight - barHeight),
                size = Size(barWidthPx, barHeight), cornerRadius = CornerRadius(8f, 8f))
        }
        drawLine(color = textSecondary.copy(0.3f), start = Offset(0f, canvasHeight),
            end = Offset(canvasWidth, canvasHeight), strokeWidth = 2f)
    }
}

/* ================== GOAL CARD ================== */
/* ================== GOAL CARD (EDITABLE) ================== */
@Composable
fun GoalCard(workoutViewModel: WorkoutViewModel = viewModel()) {
    // Goal in minutes (editable)
    var goalMinutes by remember { mutableStateOf(30) }
    var showDialog by remember { mutableStateOf(false) }

    // Today's workout minutes
    val todayMinutes by workoutViewModel.todayWorkoutMinutes.collectAsState(0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(lavender400, lavender600)))
            .padding(24.dp)
            .clickable { showDialog = true } // Open dialog to edit goal
    ) {
        Column {
            Text("My Goal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
            Text("Complete $goalMinutes min workout today", fontSize = 14.sp, color = backgroundWhite.copy(0.9f))
            Spacer(Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = (todayMinutes.coerceAtMost(goalMinutes).toFloat() / goalMinutes),
                color = backgroundWhite,
                trackColor = backgroundWhite.copy(0.35f),
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50))
            )
            Spacer(Modifier.height(8.dp))
            Text("$todayMinutes / $goalMinutes min completed", fontSize = 13.sp, color = backgroundWhite.copy(0.9f))
        }
    }

    // ===== DIALOG TO EDIT GOAL =====
    if (showDialog) {
        var input by remember { mutableStateOf(TextFieldValue(goalMinutes.toString())) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Set Daily Workout Goal") },
            text = {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Goal in minutes") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    goalMinutes = input.text.toIntOrNull()?.coerceAtLeast(1) ?: goalMinutes
                    showDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}



@Composable
fun SimpleStatBox(value: String, label: String, bg: Color, valueColor: Color) {
    Column(modifier = Modifier.width(80.dp).clip(RoundedCornerShape(18.dp)).background(bg).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = valueColor)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = textSecondary)
    }
}

/* ================== BODY METRICS (EDITABLE) ================== */
@Composable
fun BodyMetricsGrid(user: UserModel?, userViewModel: UserViewModel) {
    var showWeightDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf(TextFieldValue(user?.weight ?: "")) }
    var heightInput by remember { mutableStateOf(TextFieldValue(user?.height ?: "")) }

    val weight = user?.weight?.toDoubleOrNull() ?: 70.0
    val height = user?.height?.toDoubleOrNull() ?: 1.7
    val gender = user?.gender ?: "male"

    val bmi = calculateBMI(weight, height)
    val bodyFat = estimateBodyFat(bmi, gender).coerceIn(5.0, 40.0)
    val muscle = estimateMuscleMass(weight, bodyFat)
    val prevWeight = weight + 1.0
    val prevFat = bodyFat + 1.0
    val prevMuscle = muscle - 0.8

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MetricCard("Weight", String.format("%.1f", weight), "kg",
                formatChange(weight - prevWeight, "kg")) { showWeightDialog = true }
            MetricCard("BMI", String.format("%.1f", bmi), "", when {
                bmi < 18.5 -> "Underweight"
                bmi < 25 -> "Healthy"
                bmi < 30 -> "Overweight"
                else -> "Obese"
            })
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MetricCard("Body Fat", String.format("%.1f", bodyFat), "%", formatChange(bodyFat - prevFat, "%"))
            MetricCard("Height", String.format("%.2f", height), "m", formatChange(height - 1.7, "m")) { showHeightDialog = true }
        }
    }

    // ===== DIALOGS =====
    if (showWeightDialog) {
        AlertDialog(onDismissRequest = { showWeightDialog = false }, title = { Text("Edit Weight") },
            text = { TextField(weightInput.text, onValueChange = { weightInput = TextFieldValue(it) }, label = { Text("Weight in kg") }) },
            confirmButton = {
                TextButton(onClick = {
                    val newWeight = weightInput.text.toDoubleOrNull() ?: weight
                    val updatedUser = user?.copy(weight = newWeight.toString())
                    updatedUser?.let { userViewModel.addUserToDatabase(it.userId, it) { _, _ -> } }
                    showWeightDialog = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showWeightDialog = false }) { Text("Cancel") } }
        )
    }

    if (showHeightDialog) {
        AlertDialog(onDismissRequest = { showHeightDialog = false }, title = { Text("Edit Height") },
            text = { TextField(heightInput.text, onValueChange = { heightInput = TextFieldValue(it) }, label = { Text("Height in meters") }) },
            confirmButton = {
                TextButton(onClick = {
                    val newHeight = heightInput.text.toDoubleOrNull() ?: height
                    val updatedUser = user?.copy(height = newHeight.toString())
                    updatedUser?.let { userViewModel.addUserToDatabase(it.userId, it) { _, _ -> } }
                    showHeightDialog = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showHeightDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String, change: String, onClick: (() -> Unit)? = null) {
    Column(modifier = Modifier.width(165.dp).clip(RoundedCornerShape(26.dp)).background(backgroundWhite)
        .padding(20.dp).let { if (onClick != null) it.clickable { onClick() } else it }) {
        Text(title, fontSize = 13.sp, color = textMuted)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Text(unit, fontSize = 13.sp, color = textMuted, modifier = Modifier.padding(start = 4.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(change, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = if (change.contains("↑")) mint500 else rose500)
    }
}


@Preview
@Composable
fun PreviewAchievementScreen() {
    AchievementScreen()
}
