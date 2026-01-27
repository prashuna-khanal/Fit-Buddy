package com.example.fit_buddy.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.fit_buddy.R
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
    var showText by remember { mutableStateOf(false) }

    // Navigate only in real app
    if (!LocalInspectionMode.current) {
        val activity = context as Activity
        LaunchedEffect(Unit) {
            delay(1200)
            showText = true
            delay(2200)
            context.startActivity(
                Intent(context, LandingActivity::class.java)
            )
            activity.finish()
        }
    }

    SplashContent(showText)
}

@Composable
fun SplashContent(showText: Boolean = true) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash)
    )

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEE6FF),
            Color(0xFFD7C9FF),
            Color(0xFFC3B6F5)
        )
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 🔥 Animation Area
            Box(
                modifier = Modifier.size(360.dp),
                contentAlignment = Alignment.Center
            ) {
                if (composition != null) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                } else {
                    //CircularProgressIndicator(color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✨ Animated Text
            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(800)
                        )
            )
            {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "Fit Buddy",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your fitness journey starts here 💪",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashContent()
}
