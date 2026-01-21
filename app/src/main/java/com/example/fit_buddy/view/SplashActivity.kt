package com.example.fit_buddy.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current

    // Only navigate in real app, not in preview
    if (!LocalInspectionMode.current) {
        val activity = context as Activity
        LaunchedEffect(Unit) {
            delay(2500) // Wait 2.5 seconds
            context.startActivity(Intent(context, LoginActivity::class.java))
            activity.finish()
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    // Load Lottie animation from res/raw/splash_lottie.json
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundLightLavender), // Match ProfileScreen theme
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Lottie Animation
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(200.dp) // Slightly smaller to match soft theme
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App Name
            Text(
                text = "Fit Buddy",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Match Profile text color
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Loading Text
            Text(
                text = "App is starting, please wait...",
                fontSize = 14.sp,
                color = Color.Gray // Softer text for light background
            )

            Spacer(modifier = Modifier.height(30.dp))

            CircularProgressIndicator(color = Color(0xFF6C63FF)) // Accent color similar to Profile theme
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Preview-safe placeholder
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Fit Buddy",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "App is starting, please wait...",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(30.dp))
        CircularProgressIndicator(color = Color(0xFF6C63FF))
    }
}
