package com.example.fit_buddy.view

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.viewmodel.WorkoutViewModel
import com.example.fit_buddy.repository.UserRepoImpl
import kotlin.math.abs

/* ================== HELPER FUNCTIONS ================== */
fun calculateBMI(weightKg: Double, heightM: Double): Double {
    if (heightM <= 0.0) return 0.0
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
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val userRepo = remember { UserRepoImpl() }

    // ✅ Correct Factory for AndroidViewModel
    val userViewModel: UserViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(application, userRepo) as T
            }
        }
    )

    val workoutViewModel: WorkoutViewModel = viewModel()
    val streakData by workoutViewModel.streakData.collectAsState(WorkoutViewModel.StreakData())
    val graphData by workoutViewModel.graphData.collectAsState(WorkoutViewModel.GraphData())
    val user by userViewModel.user.observeAsState()

    // Load user data when screen opens
    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }

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

            GoalCard(workoutViewModel)
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
                    Text("${streakData.currentStreak}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Day Streak", fontSize = 14.sp, color = Color.White.copy(0.9f))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${streakData.bestStreak}", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Best Streak", fontSize = 13.sp, color = Color.White.copy(0.9f))
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
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(50)).background(Color.White),
            contentAlignment = Alignment.Center
        ) { if (checked) Text("✓", color = Color(0xFFFF4081), fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        Spacer(Modifier.height(6.dp))
        Text(day, fontSize = 11.sp, color = Color.White)
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
    val barWidth = 8.dp
    val spacing = 4.dp

    Canvas(Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 8.dp)) {
        val canvasHeight = size.height
        val barWidthPx = barWidth.toPx()
        val spacingPx = spacing.toPx()

        minutesList.forEachIndexed { index, minutes ->
            val barHeight = (minutes.toFloat() / maxMinutes) * (canvasHeight * 0.8f)
            val x = index * (barWidthPx + spacingPx)
            drawRoundRect(
                color = lavender500,
                topLeft = Offset(x, canvasHeight - barHeight),
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

/* ================== GOAL CARD ================== */
@Composable
fun GoalCard(workoutViewModel: WorkoutViewModel) {
    var goalMinutes by remember { mutableIntStateOf(30) }
    var showDialog by remember { mutableStateOf(false) }
    val todayMinutes by workoutViewModel.todayWorkoutMinutes.collectAsState(0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(lavender400, lavender600)))
            .padding(24.dp)
            .clickable { showDialog = true }
    ) {
        Column {
            Text("My Goal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Complete $goalMinutes min workout today", fontSize = 14.sp, color = Color.White.copy(0.9f))
            Spacer(Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = (todayMinutes.toFloat() / goalMinutes.toFloat()).coerceIn(0f, 1f),
                color = Color.White,
                trackColor = Color.White.copy(0.35f),
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50))
            )
            Spacer(Modifier.height(8.dp))
            Text("$todayMinutes / $goalMinutes min completed", fontSize = 13.sp, color = Color.White.copy(0.9f))
        }
    }

    if (showDialog) {
        var tempInput by remember { mutableStateOf(goalMinutes.toString()) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Set Daily Goal") },
            text = {
                OutlinedTextField(
                    value = tempInput,
                    onValueChange = { tempInput = it },
                    label = { Text("Minutes") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    goalMinutes = tempInput.toIntOrNull() ?: goalMinutes
                    showDialog = false
                }) { Text("Save") }
            }
        )
    }
}

/* ================== BODY METRICS ================== */
@Composable
fun BodyMetricsGrid(user: UserModel?, userViewModel: UserViewModel) {
    var showWeightDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }

    val weight = user?.weight?.toDoubleOrNull() ?: 70.0
    val height = user?.height?.toDoubleOrNull() ?: 1.7
    val gender = user?.gender ?: "Male"

    val bmi = calculateBMI(weight, height)
    val bodyFat = estimateBodyFat(bmi, gender)



    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard("Weight", String.format("%.1f", weight), "kg", "Target: 65kg", Modifier.weight(1f)) { showWeightDialog = true }
            MetricCard("BMI", String.format("%.1f", bmi), "", if(bmi < 25) "Healthy" else "Overweight", Modifier.weight(1f))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard("Body Fat", String.format("%.1f", bodyFat), "%", "Est. Metric", Modifier.weight(1f))
            MetricCard("Height", String.format("%.2f", height), "m", "Fixed", Modifier.weight(1f)) { showHeightDialog = true }
        }
    }

    // Dialogs for editing
    if (showWeightDialog) {
        var weightEdit by remember { mutableStateOf(weight.toString()) }
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            title = { Text("Update Weight") },
            text = { OutlinedTextField(value = weightEdit, onValueChange = { weightEdit = it }) },
            confirmButton = {
                TextButton(onClick = {
                    val updated = user?.copy(weight = weightEdit)
                    updated?.let { userViewModel.updateUserProfile(it.userId, it) { _, _ -> } }
                    showWeightDialog = false
                }) { Text("Update") }
            }
        )
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String, subtext: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(20.dp)
    ) {
        Text(title, fontSize = 13.sp, color = textMuted)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Text(unit, fontSize = 13.sp, color = textMuted, modifier = Modifier.padding(start = 4.dp))
        }
        Text(subtext, fontSize = 12.sp, color = lavender500, fontWeight = FontWeight.Medium)
    }
}