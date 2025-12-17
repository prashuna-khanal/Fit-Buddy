package com.example.fit_buddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.ui.theme.*

@Composable
fun AchievementScreen() {
    Scaffold(containerColor = backgroundLightLavender) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(padding)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Achievements",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(Modifier.height(24.dp))

            StreakCard()

            Spacer(Modifier.height(26.dp))

            GoalCard()

            Spacer(Modifier.height(26.dp))

            TotalRepsCard()

            Spacer(Modifier.height(30.dp))

            Text(
                "Body Metrics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(Modifier.height(18.dp))

            BodyMetricsGrid()

            Spacer(Modifier.height(30.dp))

            Text(
                "AI Training Sessions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(Modifier.height(16.dp))

            TrainingSessionCard("Full Body HIIT", "Today", "45 reps", "320 cal", 94)
            TrainingSessionCard("Core Strength", "Yesterday", "38 reps", "180 cal", 91)
            TrainingSessionCard("Upper Body", "2 days ago", "52 reps", "280 cal", 96)

            Spacer(Modifier.height(100.dp))
        }
    }
}

/* ================= STREAK ================= */

@Composable
fun StreakCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(streakGradientStart, streakGradientEnd)
                )
            )
            .padding(24.dp)
    ) {
        Column {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("7", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
                    Text("Day Streak", fontSize = 14.sp, color = backgroundWhite.copy(0.9f))
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("21", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
                    Text("Best Streak", fontSize = 13.sp, color = backgroundWhite.copy(0.9f))
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach {
                    DayCheck(it)
                }
            }
        }
    }
}

@Composable
fun DayCheck(day: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(backgroundWhite),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", color = rose500, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Text(day, fontSize = 11.sp, color = backgroundWhite)
    }
}

/* ================= GOAL ================= */

@Composable
fun GoalCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(listOf(lavender400, lavender600))
            )
            .padding(24.dp)
    ) {
        Column {
            Text("My Goal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = backgroundWhite)
            Text("Complete 30 squats today", fontSize = 14.sp, color = backgroundWhite.copy(0.9f))

            Spacer(Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = 22f / 30f,
                color = backgroundWhite,
                trackColor = backgroundWhite.copy(0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
            )

            Spacer(Modifier.height(8.dp))
            Text("22 / 30 completed", fontSize = 13.sp, color = backgroundWhite.copy(0.9f))
        }
    }
}

/* ================= TOTAL REPS (COLORED) ================= */

@Composable
fun TotalRepsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Total Reps Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SimpleStatBox(
                    value = "22",
                    label = "Squats",
                    bg = lavender100,
                    valueColor = lavender600
                )
                Spacer(Modifier.width(5.dp))

                SimpleStatBox(
                    value = "15",
                    label = "Push-ups",
                    bg = mint100,
                    valueColor = mint500
                )
                Spacer(Modifier.width(5.dp))

                SimpleStatBox(
                    value = "18",
                    label = "Lunges",
                    bg = rose100,
                    valueColor = rose500
                )
                Spacer(Modifier.width(5.dp))

                SimpleStatBox(
                    value = "3",
                    label = "Planks",
                    bg = lavender50,
                    valueColor = lavender500
                )
            }
        }
    }
}
@Composable
fun SimpleStatBox(
    value: String,
    label: String,
    bg: Color,
    valueColor: Color
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = textSecondary
        )
    }
}



@Composable
fun RepItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = lavender600)
        Text(label, fontSize = 13.sp, color = textSecondary)
    }
}

/* ================= BODY METRICS ================= */

@Composable
fun BodyMetricsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricCard("Weight", "72.5", "kg", "↓ 2.3 kg")
            MetricCard("BMI", "22.8", "", "↓ 0.5")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricCard("Body Fat", "18.2", "%", "↓ 1.8 %")
            MetricCard("Muscle", "58.9", "kg", "↑ 1.2 kg")
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    unit: String,
    change: String
) {
    Column(
        modifier = Modifier
            .width(165.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(backgroundWhite)
            .padding(20.dp)
    ) {
        Text(title, fontSize = 13.sp, color = textMuted)

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Text(unit, fontSize = 13.sp, color = textMuted, modifier = Modifier.padding(start = 4.dp))
        }

        Spacer(Modifier.height(6.dp))

        Text(
            change,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (change.contains("↑")) mint500 else rose500
        )
    }
}

/* ================= AI TRAINING ================= */

@Composable
fun TrainingSessionCard(
    title: String,
    day: String,
    reps: String,
    cal: String,
    accuracy: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(aiBadgeBackground, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("AI", fontSize = 11.sp, color = aiBadgeText)
                    }
                }

                Text("$accuracy%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = lavender500)
            }

            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(reps, fontSize = 13.sp, color = textSecondary)
                Text(cal, fontSize = 13.sp, color = textSecondary)
                Text(day, fontSize = 13.sp, color = textSecondary)
            }
        }
    }
}

@Preview()
@Composable
fun GreetingPreview2() {
    AchievementScreen()
}