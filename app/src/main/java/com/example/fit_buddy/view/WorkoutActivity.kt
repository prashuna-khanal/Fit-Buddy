package com.example.fit_buddy.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent

import androidx.compose.foundation.ExperimentalFoundationApi
import kotlinx.coroutines.delay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit_buddy.view.AchievementScreen
import com.example.fit_buddy.R
import com.example.fit_buddy.model.FeaturedWorkout
import com.example.fit_buddy.repository.UserRepoImpl
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.viewmodel.FeedViewModel
import com.example.fit_buddy.viewmodel.FeedViewModelFactory
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.view.OtherUserProfileScreen
import com.example.fit_buddy.viewmodel.WorkoutViewModel
import com.example.fitbuddy.repository.PoseRepo
import com.example.fitbuddy.viewmodel.PoseViewModel

class WorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(
                    application = application,
                    repository = UserRepoImpl()
                )
            )

            WorkoutScreen(navController, userViewModel)
        }
    }
}

data class NavItem(val icon: Int, val label: String)

@Composable
fun WorkoutScreen(navController: NavController, userViewModel: UserViewModel) {
    val workoutViewModel: WorkoutViewModel=viewModel  ()
    val history by workoutViewModel.workoutHistory.collectAsState(initial = emptyList())
//    observe history
//    val history by workoutViewModel.workoutHistory.collectAsState(initial=emptyList())
    val todayMins by workoutViewModel.todayWorkoutMinutes.collectAsState()
    val userProfile by userViewModel.user.observeAsState()
    val firstName = userProfile?.fullName?.split(" ")?.firstOrNull() ?: "Buddy"
    val userRepo = com.example.fit_buddy.repository.UserRepoImpl()
    var showRequestsScreen by remember { mutableStateOf(false) }
    var selectedProfileId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val feedViewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory(com.example.fit_buddy.repository.PostRepository(context), userRepo)
    )
    var showFriendList by remember { mutableStateOf(false) }
//    experienced
    val intensityXP = remember(todayMins) { "${todayMins * 20} XP" }
//    total todays duration
    val durationText = remember(todayMins) { "$todayMins Mins" }
//    vibing to the app
    val currentStatus = remember(todayMins) {
        when {
            todayMins >= 45 -> "Warrior"
            todayMins >= 20 -> "Active"
            todayMins > 0  -> "Started"
            else           -> "Resting"
        }
    }

    var selectedIndex by remember { mutableStateOf(0) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    var showHistorySheet by remember { mutableStateOf(false) }



    if (showHistorySheet) {
        WorkoutHistorySheet(
            history = history,
        ) {
            showHistorySheet = false
        }
    }

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
                0 -> WorkoutHomeScreen(userViewModel=userViewModel, userName = firstName, onSeeAllClick = {showHistorySheet=true},
                 intensityXP = intensityXP,
                    durationText = durationText,
                    currentStatus = currentStatus
                )

                1 -> {
                    when {

                        selectedProfileId != null -> {
                            OtherUserProfileScreen(
                                userId = selectedProfileId!!,
                                viewModel = feedViewModel,
                                onBack = { selectedProfileId = null }
                            )
                        }

                        showFriendList -> {
                            FriendListScreen(
                                viewModel = feedViewModel,
                                onBack = { showFriendList = false },
                                onFriendClick = { id ->
                                    selectedProfileId = id

                                }
                            )
                        }

                        showRequestsScreen -> {
                            val requests by feedViewModel.friendRequests.observeAsState(emptyList())
                            val allUsers by feedViewModel.allUsers.observeAsState(emptyList())
                            FriendRequestsScreen(
                                requests = requests,
                                allUsers = allUsers,
                                onAccept = { id ->
                                    val requestObj = requests.find { it.userId == id }
                                    requestObj?.let { feedViewModel.respondToRequest(it, true) }
                                },
                                onDelete = { id ->
                                    val requestObj = requests.find { it.userId == id }
                                    requestObj?.let { feedViewModel.respondToRequest(it, false) }
                                },
                                onProfileClick = { id -> selectedProfileId = id },
                                onBack = { showRequestsScreen = false }
                            )
                        }
                        else -> {
                            FeedSection(
                                userViewModel = userViewModel,
                                viewModel = feedViewModel,
                                onRequestsClick = {
                                    showRequestsScreen = true
                                },
                                onProfileClick = { id ->
                                    selectedProfileId = id
                                },
                                onFriendsListClick = {
                                    showFriendList = true
                                }
                            )
                        }
                    }
                }
                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (cameraPermissionGranted && viewModel != null) {
                            ExerciseActivityScreen(userViewModel)
                        } else {
                            Text("Camera permission required", color = Color.Gray)
                        }
                    }
                }
                3 -> AchievementScreen()
                4 -> ProfileScreen(feedViewModel)
            }
        }
    }
}

@Composable
fun WorkoutHomeScreen(userViewModel: UserViewModel, userName: String, onSeeAllClick: () -> Unit,
                      intensityXP: String,
                      durationText: String,
                      currentStatus: String
                      ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(userName = userName,
            stat1 = intensityXP,
            stat2 = durationText,
            stat3 = currentStatus,
            userViewModel = userViewModel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundLightLavender)
        ) {
            Spacer(Modifier.height(17.dp))
            AICoachCard()
            Spacer(Modifier.height(17.dp))
            WeeklyActivityCard(userViewModel, onSeeAllClick = onSeeAllClick)
            Spacer(Modifier.height(18.dp))
            WorkoutListSection(onSeeAllClick = onSeeAllClick)
        }
    }
}

@Composable
fun HeaderSection(userName: String,
                  stat1: String, // Intensity XP
                  stat2: String, // Duration
                  stat3: String, // Status
                  userViewModel: UserViewModel
                  ) {
    //  BMI calculation logic
    val userProfile by userViewModel.user.observeAsState()

    //  BMI manually whenever userProfile updates
    val bmiValue = remember(userProfile) {
        // Convert to string first, then to double to be safe regardless of Firebase type
        val weight = userProfile?.weight?.toString()?.toDoubleOrNull() ?: 0.0
        val height = userProfile?.height?.toString()?.toDoubleOrNull() ?: 0.0

        if (weight > 0.0 && height > 0.0) {
            val hInMeters = if (height > 3.0) height / 100.0 else height
            val bmi = weight / (hInMeters * hInMeters)
            String.format("%.1f", bmi)
        } else {
            "0.0"
        }
    }
    val greeting = getGreeting()
    val subtitle = when(greeting){
        "Good Morning ☀️" -> "Time to kickstart your day!"
        "Good Afternoon 🌤️" -> "Keep the momentum going!"
        "Good Evening 🌅" -> "Ready to crush your goal today?"
        else -> "Finish the day strong!"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = greeting, color = textMuted, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text(text = userName, color = textPrimary, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = textSecondary, fontSize = 16.sp)
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

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard("Effort", stat1, R.drawable.icon_park_solid_fire,
                Brush.verticalGradient(listOf(rose50, rose100)))

            StatCard("Today", stat2, R.drawable.heart,
                Brush.verticalGradient(listOf(lavender50, lavender100)))

            StatCard("Status", stat3, R.drawable.octicon_goal_16,
                Brush.verticalGradient(listOf(mint50, mint100)))
        }
    }
}

private fun getGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good Morning ☀️"
        in 12..16 -> "Good Afternoon 🌤️"
        in 17..20 -> "Good Evening 🌅"
        else -> "Good Night 🌙"
    }
}

@Composable
fun StatCard(title: String, value: String, icon: Int, gradient: Brush,valueFontSize: TextUnit =14.sp) {
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
fun WeeklyActivityCard(userViewModel: UserViewModel, onSeeAllClick: () -> Unit) {
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
            Text("View All",
                modifier = Modifier.clickable{ onSeeAllClick() },
                color = lavender600,
            )
        }

        Spacer(Modifier.height(20.dp))
        WeeklyBars(userViewModel)
    }
}

@Composable
fun WeeklyBars(userViewModel: UserViewModel) {
    val workoutViewModel: WorkoutViewModel = viewModel()
    val history by workoutViewModel.workoutHistory.collectAsState(initial = emptyList())
    val maxHeightDp = 160.dp
//    val fillPercents = listOf(0.5f, 0.75f, 1.0f, 0.7f, 0.8f, 0.9f, 0.6f)
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val goalminutes = 15f

    val dailyStats = remember(history) {
        val today = java.time.LocalDate.now()
        days.associateWith { dayLabel ->
            val matchingDate = (0..6).map { today.minusDays(it.toLong()) }
                .find { it.dayOfWeek.name.take(3).equals(dayLabel, ignoreCase = true) }

            history.find { it.date == matchingDate?.toString() }?.minutes ?: 0
        }
    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEach { dayLabel ->
            val minutes = dailyStats[dayLabel] ?: 0

            val fraction = (minutes / goalminutes).coerceIn(0f, 1f)
            val barHeight = if (minutes >= goalminutes)
            {maxHeightDp}
            else if(minutes > 0){
                (maxHeightDp * fraction).coerceAtLeast(12.dp)

            }else{
                4.dp
            }
            val barColor = if (minutes >= 15) lavender500 else lavender500.copy(alpha = 0.3f)//            val barHeight = when {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(maxHeightDp),
                    contentAlignment = Alignment.BottomCenter
                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(buttonLightGray)
                    )

                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(barHeight)
                            .clip(RoundedCornerShape(20.dp))
                            .background(barColor)
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    dayLabel,
                    color = if (minutes >= 15) lavender500 else textMuted,
                    fontSize = 12.sp,
                    fontWeight = if (minutes >= 15) FontWeight.Bold else FontWeight.Normal
                )
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
    image: Int,

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutListSection(onSeeAllClick: () -> Unit) {
    val context = LocalContext.current

    val featuredWorkouts = listOf(
        FeaturedWorkout("Full Body HIT", "Intermediate", "25 min", "320 cal", R.drawable.workout_1),
        FeaturedWorkout("Core Strength", "Beginner", "18 min", "210 cal", R.drawable.workout_2),
        FeaturedWorkout("Upper Body Blast", "Advanced", "30 min", "400 cal", R.drawable.workout3),
        FeaturedWorkout("Leg Day Pro", "Intermediate", "22 min", "350 cal", R.drawable.workout_4),
        FeaturedWorkout("Yoga Recovery", "Beginner", "15 min", "100 cal", R.drawable.workout_5)
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { featuredWorkouts.size }
    )

    LaunchedEffect(Unit) {
        while(true) {
            delay(3000)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % featuredWorkouts.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

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
            Text("Recommended", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Text(
                "See All",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = lavender600,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) { page ->
            val workout = featuredWorkouts[page]
            WorkoutCard(
                title = workout.title,
                level = workout.level,
                duration = workout.duration,
                calories = workout.calories,
                image = workout.image
            )
        }

        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(featuredWorkouts.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) lavender500 else Color.LightGray.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(if (pagerState.currentPage == iteration) 10.dp else 7.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWorkoutScreen() {
    val navController = rememberNavController()

    // Mock UserViewModel for preview (using correct factory)
    val mockViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            application = LocalContext.current.applicationContext as android.app.Application,
            repository = UserRepoImpl()
        )
    )

    WorkoutScreen(
        navController = navController,
        userViewModel = mockViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistorySheet(
    history: List<com.example.fit_buddy.model.WorkoutDay>,
        onDismiss: () -> Unit
) {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dailyStats = remember(history) {
        val today = java.time.LocalDate.now()
        daysOfWeek.associateWith { dayLabel ->
            val matchingDate = (0..6).map { today.minusDays(it.toLong()) }
                .find { it.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault()) == dayLabel }
            history.find { it.date == matchingDate?.toString() }?.minutes ?: 0
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
//        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = lavender500) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            val totalMinutes = dailyStats.values.sum()
            if (totalMinutes == 0) {
                Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                    Text("No activity recorded this week yet!", color = textMuted)
                }
            } else {
                Text("Activity History", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text("Total this week: $totalMinutes mins", color = lavender600, fontSize = 16.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    daysOfWeek.forEach { day ->
                        val mins = dailyStats[day] ?: 0
                        HistoryRow(day, mins)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryRow(day: String, minutes: Int) {
    val goalMinutes = 15f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundLightLavender)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            modifier = Modifier.width(45.dp),
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Box(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Color.LightGray.copy(0.3f)))
//            calculation
//            val plotWidth = when{
//                minutes >=goalMinutes -> 1.0f
//                minutes > 0 ->(minutes / goalMinutes).coerceAtMost(0.15f)
//                else -> 0f
//            }
            val progressWidth = if (minutes > 0) {
                (minutes / goalMinutes).coerceIn(0.1f, 1.0f)
            } else 0f
            val barColor = if (minutes >= 15) lavender500 else lavender500.copy(alpha = 0.3f)
            Box(
                Modifier
                    .fillMaxWidth(progressWidth)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(barColor)
            )
        }

        Text(
            text = "$minutes min",
            fontWeight = FontWeight.Bold,
            color = textSecondary,
            fontSize = 14.sp
        )
    }
}