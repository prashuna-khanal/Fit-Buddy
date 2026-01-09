package com.example.fit_buddy.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.fit_buddy.ui.theme.*
import com.example.fitbuddy.repository.PoseRepo
import com.example.fitbuddy.view.AIScreen
import com.example.fitbuddy.viewmodel.PoseViewModel
import com.example.fit_buddy.viewmodel.WorkoutViewModel
import kotlinx.coroutines.delay

class ExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExerciseActivityScreen()
        }
    }
}

@Composable
fun ExerciseActivityScreen() {
    val context = LocalContext.current
    val poseRepo = remember { PoseRepo(context) }
    val poseViewModel = remember { PoseViewModel(poseRepo) }

    // Add WorkoutViewModel here
    val workoutViewModel: WorkoutViewModel = viewModel()

    // State to track if we should show AIScreen
    var showAIScreen by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    // Pose detection to start timer
    LaunchedEffect(poseViewModel.poseState) {
        if (showAIScreen && poseViewModel.poseState != null && !isTimerRunning) {
            isTimerRunning = true
        }
    }

    // Timer logic
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (true) {
                delay(1000L)
                secondsElapsed++
            }
        }
    }

    // Summary Dialog
    if (showSummary) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    showSummary = false
                    showAIScreen = false
                    secondsElapsed = 0
                    isTimerRunning = false
                },
                    colors = ButtonDefaults.buttonColors(containerColor = lavender500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Exercises", color = Color.White)
                }
            },
            title = {
                Text("Workout Complete!", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = textPrimary)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Time", color = textSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "%02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = lavender500
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Great job keeping up with your goals!", color = textSecondary, fontSize = 14.sp)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White
        )
    }

    if (showAIScreen) {
        Box(modifier = Modifier.fillMaxSize()) {
            AIScreen(viewModel = poseViewModel)

            if (!isTimerRunning) {
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "AI is waiting for you... Position your body to start!",
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                Surface(
                    color = Color.Green.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Workout Active: %02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60),
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Complete Workout Button - Now saves time to Firebase
            Button(
                onClick = {
                    isTimerRunning = false
                    showSummary = true

                    // Save workout minutes to Firebase
                    val minutes = secondsElapsed / 60
                    if (minutes > 0) {
                        workoutViewModel.addWorkoutMinutes(minutes)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 180.dp)
                    .height(48.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Complete Workout :)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
            Text(
                text = "Exercises",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
            )

            val exercises = listOf(
                ExerciseModel("Push Ups", R.drawable.pushup, "Chest • Triceps"),
                ExerciseModel("Squats", R.drawable.squat, "Legs • Glutes"),
                ExerciseModel("Plank", R.drawable.plank, "Core Strength"),
                ExerciseModel("Lunges", R.drawable.lunge, "Leg Stability"),
                ExerciseModel("Jumping Jacks", R.drawable.jumping_jack, "Full Body Cardio"),
                ExerciseModel("Mountain Climbers", R.drawable.mountain_climber, "Core • Cardio")
            )

            exercises.forEach { exercise ->
                ExerciseCardUI(
                    exercise = exercise,
                    onClick = {
                        when (exercise.name) {
                            "Push Ups" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.PUSH_UP)
                            "Squats" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.SQUAT)
                            "Plank" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.PLANK)
                            "Lunges" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.LUNGE)
                            "Jumping Jacks" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.JUMPING_JACK)
                            "Mountain Climbers" -> poseViewModel.setExerciseType(PoseViewModel.ExerciseType.MOUNTAIN_CLIMBER)
                        }
                        showAIScreen = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = exercise.image),
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(110.dp).clip(RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = exercise.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = exercise.description, fontSize = 14.sp, color = textSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = lavender500),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Start Now", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}