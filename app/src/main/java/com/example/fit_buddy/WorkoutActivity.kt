package com.example.fit_buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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

class WorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutScreen()
        }
    }
}

@Composable
fun WorkoutScreen() {
    Scaffold {padding->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
            .padding(padding)
    ) {
        HeaderSection()
        Spacer(Modifier.height(20.dp))
        AICoachCard()
        Spacer(Modifier.height(20.dp))
        WeeklyActivityCard()
        Spacer(Modifier.height(50.dp))
        BottomNavigationBar()
    }}
}
@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(270.dp)
    ) {

        // Top row: greeting + notification icon
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Good Evening", color = textMuted, fontSize = 16.sp)
                Spacer(Modifier.height(15.dp))
                Text("Sam", color = textPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(15.dp))
                Text(
                    "Ready to crush your goals today?",
                    color = textSecondary,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(buttonLightGray)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = iconNeutralDark
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp,0.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            StatCard(
                title = "Calories",
                value = "420",
                iconColor = fireIconColor,
                gradient = Brush.verticalGradient(listOf(rose50, rose100))
            )
            StatCard(
                title = "Workouts",
                value = "2",
                iconColor = lavender500,
                gradient = Brush.verticalGradient(listOf(lavender50, lavender100))
            )
            StatCard(
                title = "Goal",
                value = "85%",
                iconColor = mint500,
                gradient = Brush.verticalGradient(listOf(mint50, mint100))
            )
        }
    }
}


@Composable
fun StatCard(title: String, value: String, iconColor: Color, gradient: Brush) {
    Column(
        modifier = Modifier
            .width(105.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        )

        {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(iconColor)
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(title, fontSize = 13.sp, color = textSecondary)
    }
}

@Composable
fun AICoachCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .border(1.dp, borderNeutral, RoundedCornerShape(25.dp))
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(aiIconGradientStart, aiIconGradientEnd)
                        )
                    )
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "AI Powered",
                color = aiBadgeText,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(aiBadgeBackground)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        Spacer(Modifier.height(15.dp))
        Text("Start AI Coach", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(
            "Get real-time form feedback and rep counting with advanced AI tracking",
            color = textSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 10.dp)
        )

        Spacer(Modifier.height(15.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = textPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Begin Training →")
        }
    }
}

@Composable
fun WeeklyActivityCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, borderNeutral, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Weekly Activity", color = textPrimary, fontWeight = FontWeight.Bold)
            Text("View All", color = lavender600, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(20.dp))

        WeeklyBars()
    }
}

@Composable
fun WeeklyBars() {
    val barHeights = listOf(80, 110, 150, 120, 130, 140, 100)

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        days.forEachIndexed { index, day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(29.dp)
                        .height(barHeights[index].dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(lavender400, lavender300)
                            )
                        )
                )
                Spacer(Modifier.height(6.dp))
                Text(day, color = textMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(lavender500),
            contentAlignment = Alignment.Center
        ) {
            Text("⚡", color = Color.White, fontSize = 28.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWorkoutScreen() {
    WorkoutScreen()
}
