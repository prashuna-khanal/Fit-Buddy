package com.example.fit_buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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

data class NavItem(val icon: Int, val label: String)

@Composable
fun WorkoutScreen() {

    val navItems = listOf(
        NavItem(R.drawable.workout, "Workouts"),
        NavItem(R.drawable.feed, "Feed"),
        NavItem(R.drawable.ai, "AI"),
        NavItem(R.drawable.man_winner, "Achievement"),
        NavItem(R.drawable.profile, "Profile"),
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundLightLavender)
                    .padding(top = 6.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ) {

                // White navigation bar background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)   // ← Full width to edges
                        .height(72.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,   // ← Icons equally spaced
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // LEFT - Workouts
                        NavItemView(
                            item = navItems[0],
                            isSelected = selectedIndex == 0,
                            onClick = { selectedIndex = 0 }
                        )

                        // FEED
                        NavItemView(
                            item = navItems[1],
                            isSelected = selectedIndex == 1,
                            onClick = { selectedIndex = 1 }
                        )

                        // GAP for Center AI Button
                        Spacer(modifier = Modifier.width(80.dp))

                        // ACHIEVEMENT
                        NavItemView(
                            item = navItems[3],
                            isSelected = selectedIndex == 3,
                            onClick = { selectedIndex = 3 }
                        )

                        // RIGHT - Profile
                        NavItemView(
                            item = navItems[4],
                            isSelected = selectedIndex == 4,
                            onClick = { selectedIndex = 4 }
                        )
                    }
                }

                // FLOATING AI BUTTON
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .offset(y = (-28).dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(listOf(lavender400, lavender500))
                        )
                        .border(2.dp, backgroundLightLavender, CircleShape)
                        .clickable { selectedIndex = 2 }
                        .zIndex(10f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = navItems[2].icon),
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundLightLavender)
                .padding(padding)
        ) {
            when (selectedIndex) {
                0 -> WorkoutHomeScreen()
                1 -> FeedSection()
//                2 -> AIScreen()
//                3 -> AchievementScreen()
//                4 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun WorkoutHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection()
        Spacer(Modifier.height(20.dp))
        AICoachCard()
        Spacer(Modifier.height(20.dp))
        WeeklyActivityCard()
        Spacer(Modifier.height(18.dp))

    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(230.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Good Evening", color = textMuted, fontSize = 16.sp)
                Spacer(Modifier.height(10.dp))
                Text("Sam", color = textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text("Ready to crush your goals today?", color = textSecondary, fontSize = 14.sp)
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(buttonLightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = iconNeutralDark
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard("Calories", "420", R.drawable.icon_park_solid_fire,
                Brush.verticalGradient(listOf(rose50, rose100)))
            StatCard("Workouts", "2", R.drawable.heart,
                Brush.verticalGradient(listOf(lavender50, lavender100)))
            StatCard("Goal", "85%", R.drawable.octicon_goal_16,
                Brush.verticalGradient(listOf(mint50, mint100)))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: Int, gradient: Brush) {
    Column(
        modifier = Modifier
            .width(115.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.32f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.height(5.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textPrimary)
        Text(title, fontSize = 13.sp, color = textSecondary)
    }
}

@Composable
fun AICoachCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(25.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(aiIconGradientStart, aiIconGradientEnd))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "AI Icon",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                "AI Powered",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(aiBadgeBackground)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                color = aiBadgeText
            )
        }

        Spacer(Modifier.height(15.dp))
        Text("Start AI Coach", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(
            "Get real-time form feedback and rep counting with advanced AI tracking",
            color = textSecondary, fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = textPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Begin Training →", color = Color.White)
        }
    }
}

@Composable
fun WeeklyActivityCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Weekly Activity", color = textPrimary, fontWeight = FontWeight.Medium, fontSize = 19.sp)
            Text("View All", color = lavender600)
        }

        Spacer(Modifier.height(20.dp))
        WeeklyBars()
    }
}

@Composable
fun WeeklyBars() {
    val maxHeightDp = 160.dp
    val fillPercents = listOf(0.5f, 0.75f, 1.0f, 0.7f, 0.8f, 0.9f, 0.6f)
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEachIndexed { index, day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(maxHeightDp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(buttonLightGray)
                    )

                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height((maxHeightDp.value * fillPercents[index]).dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.verticalGradient(listOf(lavender400, lavender300)))
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(day, color = textMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NavItemView(item: NavItem, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            tint = if (isSelected) lavender500 else textSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = item.label,
            color = if (isSelected) lavender500 else textSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}


@Preview
@Composable
fun PreviewWorkoutScreen() {
    WorkoutScreen()
}
