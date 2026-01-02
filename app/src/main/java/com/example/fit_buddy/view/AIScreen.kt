package com.example.fitbuddy.view

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.fit_buddy.model.PoseModel
import com.example.fit_buddy.ui.theme.*
import com.example.fit_buddy.view.ExerciseActivity
import com.example.fit_buddy.view.ExerciseActivityScreen
import com.example.fitbuddy.viewmodel.PoseViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@Composable
fun AIScreen(viewModel: PoseViewModel) {

    val context = LocalContext.current
    var poseResult by remember { mutableStateOf<PoseModel?>(null) }

    // ---------------- TEXT TO SPEECH ----------------
    val tts = remember { TextToSpeech(context) { } }

    LaunchedEffect(Unit) {
        delay(300)
        tts.setSpeechRate(0.85f)
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // ---------------- CAMERA PERMISSION ----------------
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // ---------------- SMART VOICE FEEDBACK ----------------
    var lastSpoken by remember { mutableStateOf("") }

    LaunchedEffect(poseResult) {
        poseResult?.let {
            if (
                it.feedback.isNotBlank() &&
                it.feedback != lastSpoken &&
                (it.status == "Correct" || it.status == "Incorrect") &&
                !tts.isSpeaking
            ) {
                lastSpoken = it.feedback
                tts.speak(it.feedback, TextToSpeech.QUEUE_FLUSH, null, null)
                delay(2500)
                lastSpoken = ""
            }
        }
    }

    // ---------------- UI ----------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundWhite)
    ) {

        // ---------------- HEADER ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    val intent = Intent(context, ExerciseActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = lavender500),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Back", color = Color.White)
            }




            Button(
                onClick = { viewModel.setExerciseType(viewModel.currentExercise) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Reset", color = Color.White)
            }
        }

        // ---------------- CAMERA ----------------
        if (hasCameraPermission) {
            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            val executor = Executors.newSingleThreadExecutor()
                            imageAnalysis.setAnalyzer(executor) { imageProxy ->
                                imageProxy.toBitmap()?.let {
                                    viewModel.processFrame(it)
                                    poseResult = viewModel.poseState
                                }
                                imageProxy.close()
                            }

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                context as ComponentActivity,
                                CameraSelector.DEFAULT_FRONT_CAMERA,
                                preview,
                                imageAnalysis
                            )

                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ---------------- STATS ----------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        viewModel.currentExercise.name.replace("_", " "),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        StatItem(
                            "Status",
                            poseResult?.status ?: "Ready",
                            when (poseResult?.status) {
                                "Correct" -> Color.Green
                                "Incorrect" -> Color.Red
                                else -> textSecondary
                            }
                        )

                        StatItem(
                            "Angle",
                            "${poseResult?.accuracy ?: 0}°",
                            lavender500
                        )

                        StatItem(
                            "Reps",
                            when (viewModel.currentExercise) {
                                PoseViewModel.ExerciseType.SQUAT -> viewModel.squatCount.toString()
                                PoseViewModel.ExerciseType.PUSH_UP -> viewModel.pushUpCount.toString()
                                PoseViewModel.ExerciseType.JUMPING_JACK -> viewModel.jumpingJackCount.toString()
                                PoseViewModel.ExerciseType.MOUNTAIN_CLIMBER -> viewModel.mountainClimberCount.toString()
                                else -> "0"
                            },
                            lavender600
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        poseResult?.feedback ?: "Position yourself",
                        color = textPrimary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(buttonLightGray, RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }

        } else {

            // ---------------- PERMISSION UI ----------------
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Camera permission required",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = lavender500)
                ) {
                    Text("Allow Camera", color = Color.White)
                }
            }
        }
    }
}

// ---------------- STAT ITEM ----------------
@Composable
private fun StatItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 13.sp, color = textSecondary)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
