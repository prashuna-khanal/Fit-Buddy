package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.R

//
@Composable
fun AchievementScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())

                .padding(padding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                Text("hello", modifier = Modifier.clickable())
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_ios_new_24),
                    contentDescription = null
                )
                Box(
                    modifier = Modifier.weight(1f), // Takes up remaining horizontal space
                    contentAlignment = Alignment.Center // Centers content within the Box
                ) {
                    Text(
                        text = "Achievements",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp)
                    )
                }

            }
            Spacer(modifier = Modifier.height(20.dp))

            // ------------------- STREAK CARD -------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("My Streaks", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Icon(
                            painter = painterResource(R.drawable.icon_park_solid_fire),
                            contentDescription = null,
                            tint = Color(0xFFFF5722)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StreakBox("7", "Current", Color(0xFFFF7043))
                        StreakBox("15", "Longest", Color(0xFFFFA726))
                        StreakBox("24", "Total Days", Color(0xFFAB47BC))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ------------------- GOAL CARD -------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEF5CDA)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "My Goal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

//                        Icon(
//                            painter = painterResource(R.drawable.baseline_emoji_events_24),
//                            contentDescription = null,
//                            tint = Color.White
//                        )
                    }

                    Text(
                        "Complete 30 squats today",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = 22f / 30f,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Text(
                        "22/30 completed",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ------------------- TOTAL REPS CARD -------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total Reps Today",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            painter = painterResource(R.drawable.outline_bar_chart_24),
                            contentDescription = null,
                            tint = Color(0xFFE91E63)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RepBox("22", "Squats", Color(0xFFEF5350))
                        RepBox("15", "Push-ups", Color(0xFF42A5F5))
                        RepBox("18", "Lunges", Color(0xFF26A69A))
                        RepBox("3", "Planks", Color(0xFFFF7043))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))


            // ------------------- BODY METRICS -------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text(
                        "Body Metrics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox("Weight", "72.5 kg", "↓ 2.3 kg")
                        Spacer(modifier = Modifier.width(10.dp))
                        MetricBox("BMI", "22.8", "↓ 0.5")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox("Body Fat", "18.2%", "↓ 1.8%")
                        Spacer(modifier = Modifier.width(10.dp))
                        MetricBox("Muscle", "58.9 kg", "↑ 1.2 kg")
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))


            // ------------------- AI TRAINING SESSIONS -------------------
            Text(
                "AI Training Sessions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            TrainingSessionCard("Full Body HIIT", "Today", "45 reps", "320 cal", 94)
            TrainingSessionCard("Core Strength", "Yesterday", "38 reps", "180 cal", 91)
            TrainingSessionCard("Upper Body", "2 days ago", "52 reps", "280 cal", 96)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ------------------- SMALL COMPONENTS -------------------

@Composable
fun StreakBox(value: String, label: String, bgColor: Color) {
    Column(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun RepBox(value: String, label: String, color: Color) {
    Column(
        modifier = Modifier
            .background(color, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White, fontSize = 12.sp)
    }
}
@Composable
fun MetricBox(title: String, value: String, change: String) {
    Column(
        modifier = Modifier
//            .weight(1f)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
        Text(
            change,
            fontSize = 12.sp,
            color = if (change.contains("↑")) Color(0xFF4CAF50) else Color(0xFFE91E63),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun TrainingSessionCard(title: String, day: String, reps: String, cal: String, accuracy: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD1C4E9), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("AI", fontSize = 10.sp, color = Color(0xFF512DA8))
                    }
                }

                Text("$accuracy% Accuracy", fontSize = 13.sp, color = Color(0xFF7E57C2))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(reps, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(cal, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(day, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AchievementScreen()
}