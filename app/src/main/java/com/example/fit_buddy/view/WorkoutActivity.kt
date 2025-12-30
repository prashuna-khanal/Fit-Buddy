package com.example.fit_buddy.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_buddy.AchievementScreen
import com.example.fit_buddy.R
//import androidx.core.app.ComponentActivity
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.viewmodel.FeedViewModel
import com.example.fit_buddy.viewmodel.FeedViewModelFactory
import com.example.fitbuddy.repository.PoseRepo
import com.example.fitbuddy.view.AIScreen
import com.example.fitbuddy.viewmodel.PoseViewModel
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
    var showRequestsScreen by remember { mutableStateOf(false) }
    var selectedProfileId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val feedViewModel: FeedViewModel = viewModel(
       factory = FeedViewModelFactory(com.example.fit_buddy.repository.PostRepository(context))
    )


    var selectedIndex by remember { mutableStateOf(0) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted = granted
    }

    // Launch permission check whenever screen appears or state changes
    LaunchedEffect(cameraPermissionGranted) {
        if (!cameraPermissionGranted) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Initialize ViewModel only after permission granted
    val viewModel: PoseViewModel? = remember(cameraPermissionGranted) {
        if (cameraPermissionGranted) PoseViewModel(PoseRepo(context)) else null
    }

    val navItems = listOf(
        NavItem(R.drawable.workout, "Workouts"),
        NavItem(R.drawable.feed, "Feed"),
        NavItem(R.drawable.ai, "AI"),
        NavItem(R.drawable.icons8_man_winner_50, "Achievement"),
        NavItem(R.drawable.profile, "Profile"),
    )

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                // MAIN NAVIGATION BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItemView(navItems[0], selectedIndex == 0) { selectedIndex = 0 }
                    NavItemView(navItems[1], selectedIndex == 1) { selectedIndex = 1 }

                    Spacer(modifier = Modifier.width(80.dp))

                    NavItemView(navItems[3], selectedIndex == 3) { selectedIndex = 3 }
                    NavItemView(navItems[4], selectedIndex == 4) { selectedIndex = 4 }
                }

                // FLOATING AI BUTTON
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-20).dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(listOf(lavender400, lavender500))
                        )
                        .shadow(16.dp, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .clickable { selectedIndex = 2 }
                        .zIndex(50f),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            when (selectedIndex) {
                0 -> WorkoutHomeScreen()
                1 -> {
                    // 2. Integration with your Social Module
                    if (selectedProfileId != null) {
                        OtherUserProfileScreen(
                            userId = selectedProfileId!!,
                            viewModel = feedViewModel,
                            onBack = { selectedProfileId = null }
                        )
                    } else if (showRequestsScreen) {
                        // This assumes you observe the requests list in this screen
                        val requests by feedViewModel.friendRequests.observeAsState(emptyList())
                        FriendRequestsScreen(
                            requests = requests,
                            onAccept = { id -> feedViewModel.respondToRequest(id, true) },
                            onDelete = { id -> feedViewModel.respondToRequest(id, false) },
                            onProfileClick = { id -> selectedProfileId = id },
                            onBack = { showRequestsScreen = false }
                        )
                    } else {
                        FeedSection(
                            viewModel = feedViewModel,
                            onRequestsClick = { showRequestsScreen = true },
                            onProfileClick = { id -> selectedProfileId = id }
                        )
                    }
                }
                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (cameraPermissionGranted && viewModel != null) {
                            ExerciseActivityScreen()

                        } else {
                            Text("Camera permission required", color = Color.Gray)
                        }
                    }
                }
                3 -> AchievementScreen()
                4 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun WorkoutHomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        HeaderSection()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundLightLavender)
        ) {
            Spacer(Modifier.height(17.dp))
            AICoachCard()
            Spacer(Modifier.height(17.dp))
            WeeklyActivityCard()
            Spacer(Modifier.height(18.dp))
            WorkoutListSection()
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(240.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Good Evening", color = textMuted, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(10.dp))
                Text("Sam", color = textPrimary, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text("Ready to crush your goals today?", color = textSecondary, fontSize = 16.sp)
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

        Spacer(Modifier.height(8.dp))

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
        Text(title, fontSize = 14.sp, color = textSecondary)
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
        Spacer(Modifier.height(10.dp))
        Text(
            "Get real-time form feedback and rep counting with advanced AI tracking",
            color = textSecondary, fontSize = 15.sp
        )

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = textPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Begin Training →", color = Color.White, fontSize = 16.sp)
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
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
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

@Composable
fun WorkoutCard(
    title: String,
    level: String,
    duration: String,
    calories: String,
    image: Int
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
    ) {

        Column {

            Image(
                painter = painterResource(id = image),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(
                title,
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconamoon_clock),
                        contentDescription = null,
                        tint = lavender500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(duration, color = textSecondary, fontSize = 16.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_park_solid_fire),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(calories, color = textSecondary, fontSize = 16.sp)
                }
            }
        }

        Text(
            level,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 12.dp, end = 12.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(12.dp))
                .background(lavender500)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun WorkoutListSection() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recommended",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Text(
                "See All",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = lavender600,
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, ExerciseActivity::class.java))
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        WorkoutCard(
            title = "Full Body HIT",
            level = "Intermediate",
            duration = "25 min",
            calories = "320 cal",
            image = R.drawable.workout_1
        )

        Spacer(Modifier.height(16.dp))

        WorkoutCard(
            title = "Core Strength",
            level = "Beginner",
            duration = "18 min",
            calories = "210 cal",
            image = R.drawable.workout_2
        )
    }
}

@Preview
@Composable
fun PreviewWorkoutScreen() {
    WorkoutScreen()
}
