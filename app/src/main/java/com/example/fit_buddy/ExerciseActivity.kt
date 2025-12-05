package com.example.fit_buddy

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.lavender500
import com.example.fit_buddy.ui.theme.textPrimary
import com.example.fit_buddy.ui.theme.textSecondary


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

    val exercises = listOf(
        Exercise("Push Ups", R.drawable.pushup, "Chest • Triceps"),
        Exercise("Squats", R.drawable.squat, "Legs • Glutes"),
        Exercise("Plank", R.drawable.plank, "Core Strength"),
        Exercise("Lunges", R.drawable.lunge, "Leg Stability"),
        Exercise("Jumping Jacks", R.drawable.jumping_jack, "Full Body Cardio"),
        Exercise("Mountain Climbers", R.drawable.mountain_climber, "Core • Cardio")
    )

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
        exercises.forEach { exercise ->
            ExerciseCardUI(exercise)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ExerciseCardUI(exercise: Exercise) {
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
                    onClick = { /* TODO: open AI tracking */ },
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

@Preview(showBackground = true)
@Composable
fun ExerciseActivityPreview() {
    ExerciseActivityScreen()
}
