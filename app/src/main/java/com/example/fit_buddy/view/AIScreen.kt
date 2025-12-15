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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.fit_buddy.model.PoseModel
import com.example.fitbuddy.viewmodel.PoseViewModel
import java.util.concurrent.Executors

@Composable
fun AIScreen(viewModel: PoseViewModel) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    // Initialize TTS
    val tts = remember {
        TextToSpeech(context) {}
    }

    // Clean up TTS when composable leaves
    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    var poseResult by remember { mutableStateOf<PoseModel?>(null) }
    var lastSpokenFeedback by remember { mutableStateOf("") }

    // Camera permission
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted = granted
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (cameraPermissionGranted) {
        val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

        Column(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy ->
                        val bitmap = imageProxy.toBitmap() ?: return@setAnalyzer
                        viewModel.processFrame(bitmap)
                        poseResult = viewModel.poseState
                        imageProxy.close()
                    }

                    try {
                        activity?.let { act ->
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                act,
                                cameraSelector,
                                preview,
                                imageAnalyzer
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            )

            // Display pose info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Feedback: ${poseResult?.feedback ?: "Waiting..."}")
                Text("Squat count: ${viewModel.repo.repCount}")

            }
        }

        LaunchedEffect(poseResult) {
            poseResult?.let { result ->
                val feedback = result.feedback
                if (feedback.isNotBlank() && feedback != lastSpokenFeedback) {
                    lastSpokenFeedback = feedback
                    if (!tts.isSpeaking) {
                        tts.speak(feedback, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            }
        }


    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Camera permission required to use AI Coach")
        }
    }
}
