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
            SplashScreen1()
        }
    }
}
//splash screen with animation
@Composable
fun SplashScreen1() {
    val context = LocalContext.current

    // Run navigation only in real app (not Preview)
    if (!LocalInspectionMode.current) {
        val activity = context as Activity
        LaunchedEffect(Unit) {
            delay(2500)
            context.startActivity(
                Intent(context, LandingActivity::class.java)
            )
            activity.finish()
        }
    }

    SplashContent1()
}

@Composable
fun SplashContent1() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundLightLavender),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            if (LocalInspectionMode.current) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.LightGray)
                )
            } else {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.burpie)
                )

                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(200.dp)
                )
            }

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

            CircularProgressIndicator(
                color = Color(0xFF6C63FF)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview1() {
    SplashContent1()
}
