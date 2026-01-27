package com.example.fit_buddy.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_buddy.R
import com.example.fit_buddy.model.ExerciseModel
import com.example.fit_buddy.repository.UserRepoImpl
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.viewmodel.UserViewModelFactory
import com.example.fit_buddy.viewmodel.WorkoutViewModel
import com.example.fitbuddy.repository.PoseRepo
import com.example.fitbuddy.view.AIScreen
import com.example.fitbuddy.viewmodel.PoseViewModel
import kotlinx.coroutines.delay

class ExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(application, UserRepoImpl())
            )
            ExerciseActivityScreen(userViewModel)
        }
    }
}

@Composable
fun ExerciseActivityScreen(userViewModel: UserViewModel) {
    val context = LocalContext.current
    val poseRepo = remember { PoseRepo(context) }
    val poseViewModel: PoseViewModel = viewModel { PoseViewModel(poseRepo) }
    val workoutViewModel: WorkoutViewModel = viewModel()

    var showAIScreen by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    LaunchedEffect(poseViewModel.poseState) {

        if (showAIScreen && poseViewModel.poseState != null && !isTimerRunning) {
            isTimerRunning = true

        }
    }

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (isTimerRunning) {
                delay(1000L)
                secondsElapsed++
            }
        }
    }

    if (showSummary) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        showSummary = false
                        showAIScreen = false
                        secondsElapsed = 0
                        isTimerRunning = false
                    },
                    colors = ButtonDefaults.buttonColors(lavender500),
                    shape = RoundedCornerShape(12.dp)

                ) {
                    Text("Back to Exercises", color = Color.White)

                }
            },
            title = { Text("Workout Complete!", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = textPrimary) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Total Time", color = textSecondary, fontSize = 14.sp)
                    Text(
                        "%02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = lavender500
                    )
                    Text("Great job!", color = textSecondary, fontSize = 14.sp)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White
        )
    }

    if (showAIScreen) {
        Box(Modifier.fillMaxSize()) {
            AIScreen(poseViewModel)

            val statusText = if (!isTimerRunning) "Position your body to start!" else "Active: %02d:%02d"
            val statusColor = if (!isTimerRunning) Color.Black else Color.Green

            Surface(
                color = statusColor.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (!isTimerRunning) statusText else statusText.format(secondsElapsed / 60, secondsElapsed % 60),
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Button(
                onClick = {
                    isTimerRunning = false
                    showSummary = true
                    val minutes = secondsElapsed / 60
                    if (minutes > 0) workoutViewModel.addWorkoutMinutes(minutes)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 180.dp)
                    .height(48.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Complete Workout", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundLightLavender)
                .verticalScroll(rememberScrollState())
                .padding(top = 18.dp)
        ) {
            Text("Exercises", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = textPrimary, modifier = Modifier.padding(start = 20.dp, bottom = 10.dp))

            val exercises = listOf(
                ExerciseModel("Push Ups", R.drawable.pushup, "Chest • Triceps"),
                ExerciseModel("Squats", R.drawable.squat, "Legs • Glutes"),
                ExerciseModel("Plank", R.drawable.plank, "Core Strength"),
                ExerciseModel("Lunges", R.drawable.lunge, "Leg Stability"),
                ExerciseModel("Jumping Jacks", R.drawable.jumping_jack, "Full Body Cardio"),
                ExerciseModel("Mountain Climbers", R.drawable.mountain_climber, "Core • Cardio")
            )

            exercises.forEach { ex ->
                ExerciseCardUI(ex) {
                    // Set exercise type and show AI screen
                    when (ex.name) {
                        "Push Ups" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.PUSH_UP)
                        "Squats" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.SQUAT)
                        // ... other cases
                    }
                    showAIScreen = true
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun ExerciseCardUI(exercise: ExerciseModel, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(exercise.image),
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(110.dp).clip(RoundedCornerShape(20.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(exercise.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text(exercise.description, fontSize = 14.sp, color = textSecondary)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(lavender500),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Start Now", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}