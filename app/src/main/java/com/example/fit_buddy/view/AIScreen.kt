package com.example.fitbuddy.view

import android.Manifest
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
import com.example.fitbuddy.viewmodel.PoseViewModel
import java.util.concurrent.Executors
import kotlinx.coroutines.delay

@Composable
fun AIScreen(viewModel: PoseViewModel) {
    val context = LocalContext.current
    var poseResult by remember { mutableStateOf<PoseModel?>(null) }

    // Initialize TTS (simpler approach)
    val tts = remember {
        TextToSpeech(context, null)
    }

    // Set TTS properties
    LaunchedEffect(tts) {
        delay(300) // Give TTS time to initialize
        tts.setSpeechRate(0.85f)
    }

    // Clean up TTS
    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // Camera permission state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    // Request permission
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Smart voice feedback with less frequent speaking
    var lastSpokenFeedback by remember { mutableStateOf("") }

    LaunchedEffect(poseResult) {
        poseResult?.let { result ->
            val feedback = result.feedback

            // Only speak for important feedback
            val shouldSpeak = feedback.isNotBlank() &&
                    feedback != lastSpokenFeedback &&
                    (result.status == "Correct" || result.status == "Incorrect") &&
                    !tts.isSpeaking

            if (shouldSpeak) {
                lastSpokenFeedback = feedback
                tts.speak(feedback, TextToSpeech.QUEUE_FLUSH, null, null)

                // Add cooldown
                delay(3000)
                lastSpokenFeedback = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundWhite)
    ) {
        // Simple header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { (context as? ComponentActivity)?.finish() },
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = lavender500)
            ) {
                Text("← Back", color = Color.White)
            }

            Button(
                onClick = { viewModel.resetCounts() },
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Reset", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (hasCameraPermission) {
            // Camera preview with error handling
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(top = 70.dp, bottom = 10.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)

                        try {
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                            cameraProviderFuture.addListener({
                                try {
                                    val cameraProvider = cameraProviderFuture.get()

                                    val preview = Preview.Builder()
                                        .build()
                                        .also {
                                            it.setSurfaceProvider(previewView.surfaceProvider)
                                        }

                                    val imageAnalyzer = ImageAnalysis.Builder()
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()

                                    val cameraExecutor = Executors.newSingleThreadExecutor()

                                    imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy ->
                                        try {
                                            val bitmap = imageProxy.toBitmap()
                                            if (bitmap != null) {
                                                viewModel.processFrame(bitmap)
                                                poseResult = viewModel.poseState
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        } finally {
                                            imageProxy.close()
                                        }
                                    }

                                    // Use front camera
                                    val cameraSelector = CameraSelector.Builder()
                                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                                        .build()

                                    cameraProvider.unbindAll()
                                    cameraProvider.bindToLifecycle(
                                        context as? ComponentActivity ?: return@addListener,
                                        cameraSelector,
                                        preview,
                                        imageAnalyzer
                                    )

                                } catch (e: Exception) {
                                    // Log error but don't crash
                                    android.util.Log.e("CameraError", "Camera setup failed", e)
                                }
                            }, ContextCompat.getMainExecutor(ctx))

                        } catch (e: Exception) {
                            android.util.Log.e("CameraError", "Camera initialization failed", e)
                        }

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Stats display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    // Current exercise
                    Text(
                        text = viewModel.currentExercise.name.replace("_", " "),
                        color = textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Status", color = textSecondary, fontSize = 14.sp)
                            Text(
                                poseResult?.status ?: "Ready",
                                color = when (poseResult?.status) {
                                    "Correct" -> Color.Green
                                    "Incorrect" -> Color.Red
                                    else -> textSecondary
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Angle", color = textSecondary, fontSize = 14.sp)
                            Text(
                                "${poseResult?.accuracy ?: 0}°",
                                color = lavender500,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Reps", color = textSecondary, fontSize = 14.sp)
                            Text(
                                if (viewModel.currentExercise == PoseViewModel.ExerciseType.SQUAT)
                                    "${viewModel.squatCount}"
                                else
                                    "${viewModel.pushUpCount}",
                                color = lavender600,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Feedback
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .background(
                                when (poseResult?.status) {
                                    "Correct" -> Color.Green.copy(alpha = 0.1f)
                                    "Incorrect" -> Color.Red.copy(alpha = 0.1f)
                                    else -> buttonLightGray
                                },
                                RoundedCornerShape(50.dp)
                            )
                            .padding(20.dp)
                    ) { }

                }
            }

        } else {
            // Permission view
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Camera Access Needed",
                    color = textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = lavender500)
                ) {
                    Text("Allow Camera", color = Color.White)
                }
            }
        }
    }
}