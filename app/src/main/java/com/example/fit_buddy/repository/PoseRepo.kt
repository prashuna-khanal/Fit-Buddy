package com.example.fitbuddy.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.fit_buddy.model.PoseModel
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.acos
import kotlin.math.sqrt

class PoseRepo(context: Context) {

    private val poseDetector: PoseLandmarker
    var repCount = 0
        private set

    private var squatPhase = SquatPhase.STANDING

    enum class SquatPhase {
        STANDING, GOING_DOWN, SQUAT, STANDING_UP
    }

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

    fun detectPose(bitmap: Bitmap): PoseModel? {
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = poseDetector.detect(mpImage)

        // If no person detected, return null
        if (result.landmarks().isEmpty()) return null

        val lm = result.landmarks()[0]
        val hip = lm[23]
        val knee = lm[25]
        val ankle = lm[27]

        val kneeAngle = calculateAngle(
            hip.x().toDouble(), hip.y().toDouble(),
            knee.x().toDouble(), knee.y().toDouble(),
            ankle.x().toDouble(), ankle.y().toDouble()
        )

        var feedback = "Adjust your posture"
        var status = "Incorrect"

        // Use squat phases for realistic feedback
        when (squatPhase) {
            SquatPhase.STANDING -> {
                if (kneeAngle < 160 && kneeAngle > 120) {
                    squatPhase = SquatPhase.GOING_DOWN
                    feedback = ""
                } else {
                    feedback = "Stand straight"
                }
            }

            SquatPhase.GOING_DOWN -> {
                if (kneeAngle < 100) {
                    squatPhase = SquatPhase.SQUAT
                    feedback = ""
                    status = "Correct"
                } else {
                    feedback = "Bend deeper, keep back straight"
                }
            }

            SquatPhase.SQUAT -> {
                if (kneeAngle > 140) {
                    squatPhase = SquatPhase.STANDING_UP
                    repCount++
                    feedback = "Well done! Rep ${repCount}"
                    status = "Correct"
                } else {
                    feedback = "Hold squat position"
                    status = "Correct"
                }
            }

            SquatPhase.STANDING_UP -> {
                if (kneeAngle > 160) {
                    squatPhase = SquatPhase.STANDING
                    feedback = ""
                    status = "Correct"
                } else {
                    feedback = "Finish standing"
                }
            }
        }

        return PoseModel(status, kneeAngle.toInt(), feedback)
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
}
