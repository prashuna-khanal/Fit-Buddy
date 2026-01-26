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
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fitbuddy.repository.PoseRepo
import com.example.fitbuddy.view.AIScreen
import com.example.fitbuddy.viewmodel.PoseViewModel
import kotlinx.coroutines.delay

class ExerciseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val userRepo = com.example.fit_buddy.repository.UserRepoImpl()
            val userViewModel: UserViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return UserViewModel(userRepo,this@ExerciseActivity) as T
                }
            })
            ExerciseActivityScreen(userViewModel=userViewModel)
        }
    }
}

@Composable
fun ExerciseActivityScreen(userViewModel: UserViewModel) {
    val context = LocalContext.current
    val poseRepo = remember { PoseRepo(context) }
    val poseViewModel = remember { PoseViewModel(poseRepo) }

    // State to track if we should show AIScreen
    var showAIScreen by remember { mutableStateOf(false) }
//    state variables for timer and showing time how much is done
    var secondsElapsed by remember{ mutableIntStateOf(0)}
    var isTimerRunning by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }
//    Pose dection to start timer
    LaunchedEffect(poseViewModel.poseState) {
        if(showAIScreen && poseViewModel.poseState!=null && !isTimerRunning){
            isTimerRunning=true
        }
    }
//    timer logic
    LaunchedEffect(isTimerRunning) {
        if(isTimerRunning){
            while (true){
                delay(1000L)
                secondsElapsed++
            }
        }
    }
//    showing summary of time when completed button is clicked
    if(showSummary){
        AlertDialog(
            onDismissRequest = {}, //no dismiss
            confirmButton = {
                Button(onClick = {
                    val minutesEarned = secondsElapsed / 60

                    val today = java.time.LocalDate.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("EEE")
                    )


                    userViewModel.updateWorkoutTime(today, minutesEarned)
                    showSummary=false
                    showAIScreen=false //ensure it returns to activities/exerices
                    secondsElapsed=0 //reset timer
                },
                    colors = ButtonDefaults.buttonColors(contentColor = lavender500),
                    shape = RoundedCornerShape(12.dp)

                ) {
                    Text("Back to Exercies", color = Color.White)
                }
            },
            title = {
                Text("Workout Complete!" , fontWeight = FontWeight.Bold, fontSize = 22.sp, color = textPrimary)
            },
            text = {
                Column (modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Total Time", color = textSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
//                  minutes: seconds  , one digit vaye 0 add garera dekhaune %d show integer valye ani 2 alsway show two digits eg; 02:05
                    Text(text = "%02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60),
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
        // Show AIScreen with current exercise
        Box(modifier = Modifier.fillMaxSize()) {


            AIScreen(viewModel = poseViewModel)
//            let user know that time started
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
//                  small recording indicator or the live timer
                Surface(
                    color = Color.Green.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Workout Active: %02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60),
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
//            button for complete
            Button(onClick = {

                isTimerRunning=false //stop counting
                showSummary = true //show result once stopped
            },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(end = 20.dp, bottom = 180.dp)
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
        // Show Exercise List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundLightLavender)
                .verticalScroll(rememberScrollState())
                .padding(top = 18.dp)
        ) {
            // Top Header
            Text(
                text = "Exercises",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
            )

            // List of Exercise Cards
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
                            "Push Ups" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.PUSH_UP)

                            "Squats" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.SQUAT)

                            "Plank" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.PLANK)

                            "Lunges" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.LUNGE)

                            "Jumping Jacks" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.JUMPING_JACK)

                            "Mountain Climbers" ->
                                poseViewModel.setExerciseType(PoseViewModel.ExerciseType.MOUNTAIN_CLIMBER)
                        }
//                        start timer when start now is clicked
                        showAIScreen= true
//                        isTimerRunning=true

                    }

                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ExerciseCardUI(
    exercise: ExerciseModel,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise Image
            Image(
                painter = painterResource(id = exercise.image),
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = exercise.description,
                    fontSize = 14.sp,
                    color = textSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // START NOW Button
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