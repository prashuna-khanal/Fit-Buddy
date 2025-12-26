package com.example.fitbuddy.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.fit_buddy.model.PoseModel
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlin.math.acos
import kotlin.math.sqrt

class PoseRepo(context: Context) {

    private val poseDetector: PoseLandmarker

    // Squat tracking
    private var squatRepCount = 0
    private var isInSquat = false
    private var hasCompletedSquat = false

    // Push-up tracking
    private var pushUpRepCount = 0
    private var isInPushUpDown = false
    private var hasCompletedPushUp = false

    init {
        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath("pose_landmarker_lite.task")
                    .build()
            )
            .setNumPoses(1)
            .build()

        poseDetector = PoseLandmarker.createFromOptions(context, options)
    }

    // SIMPLIFIED SQUAT DETECTION
    fun detectSquat(bitmap: Bitmap): PoseModel? {
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = poseDetector.detect(mpImage)

        if (result.landmarks().isEmpty()) {
            return PoseModel("Ready", 0, "Stand in frame")
        }

        val lm = result.landmarks()[0]
        val hip = lm[23]
        val knee = lm[25]
        val ankle = lm[27]

        val kneeAngle = calculateAngle(
            hip.x().toDouble(), hip.y().toDouble(),
            knee.x().toDouble(), knee.y().toDouble(),
            ankle.x().toDouble(), ankle.y().toDouble()
        )

        var feedback = "Stand straight"
        var status = "Ready"

        // SIMPLE LOGIC: Count when going from squat to stand
        when {
            // Standing position
            kneeAngle > 160 -> {
                if (hasCompletedSquat) {
                    squatRepCount++
                    feedback = "Rep $squatRepCount ✓"
                    status = "Correct"
                    hasCompletedSquat = false
                } else {
                    feedback = "Ready to squat"
                    status = "Ready"
                }
                isInSquat = false
            }

            // Good squat depth
            kneeAngle in 75.0..105.0 -> {
                isInSquat = true
                feedback = "Good depth"
                status = "Correct"

                // Mark that we've reached good depth
                if (!hasCompletedSquat) {
                    hasCompletedSquat = true
                }
            }

            // Too deep
            kneeAngle < 75 -> {
                feedback = "Too deep"
                status = "Incorrect"
            }

            // Too shallow
            kneeAngle in 105.0..140.0 -> {
                feedback = "Bend deeper"
                status = "Incorrect"
            }

            // Transition
            else -> {
                feedback = if (kneeAngle < 140) "Going down" else "Coming up"
                status = "Correct"
            }
        }

        return PoseModel(status, kneeAngle.toInt(), feedback)
    }

    // SIMPLIFIED PUSH-UP DETECTION
    fun detectPushUp(bitmap: Bitmap): PoseModel? {
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = poseDetector.detect(mpImage)

        if (result.landmarks().isEmpty()) {
            return PoseModel("Ready", 0, "Get in plank")
        }

        val lm = result.landmarks()[0]
        val shoulder = lm[11]
        val elbow = lm[13]
        val wrist = lm[15]

        val elbowAngle = calculateAngle(
            shoulder.x().toDouble(), shoulder.y().toDouble(),
            elbow.x().toDouble(), elbow.y().toDouble(),
            wrist.x().toDouble(), wrist.y().toDouble()
        )

        var feedback = "Arms straight"
        var status = "Ready"

        // SIMPLE LOGIC: Count when going from down to up
        when {
            // Top position (arms straight)
            elbowAngle > 160 -> {
                if (hasCompletedPushUp) {
                    pushUpRepCount++
                    feedback = "Rep $pushUpRepCount ✓"
                    status = "Correct"
                    hasCompletedPushUp = false
                } else {
                    feedback = "Ready to go down"
                    status = "Ready"
                }
                isInPushUpDown = false
            }

            // Good push-up depth
            elbowAngle in 75.0..100.0 -> {
                isInPushUpDown = true
                feedback = "Good depth"
                status = "Correct"

                // Mark that we've reached good depth
                if (!hasCompletedPushUp) {
                    hasCompletedPushUp = true
                }
            }

            // Too low
            elbowAngle < 75 -> {
                feedback = "Too low"
                status = "Incorrect"
            }

            // Too shallow
            elbowAngle in 100.0..140.0 -> {
                feedback = "Go lower"
                status = "Incorrect"
            }

            // Transition
            else -> {
                feedback = if (elbowAngle < 140) "Going down" else "Pushing up"
                status = "Correct"
            }
        }

        return PoseModel(status, elbowAngle.toInt(), feedback)
    }

    private fun calculateAngle(ax: Double, ay: Double, bx: Double, by: Double, cx: Double, cy: Double): Double {
        val abx = ax - bx
        val aby = ay - by
        val cbx = cx - bx
        val cby = cy - by

        val dot = abx * cbx + aby * cby
        val mag1 = sqrt(abx * abx + aby * aby)
        val mag2 = sqrt(cbx * cbx + cby * cby)

        return Math.toDegrees(acos(dot / (mag1 * mag2)))
    }

    fun getSquatCount(): Int = squatRepCount
    fun getPushUpCount(): Int = pushUpRepCount

    fun resetSquat() {
        squatRepCount = 0
        isInSquat = false
        hasCompletedSquat = false
    }

    fun resetPushUp() {
        pushUpRepCount = 0
        isInPushUpDown = false
        hasCompletedPushUp = false
    }
}