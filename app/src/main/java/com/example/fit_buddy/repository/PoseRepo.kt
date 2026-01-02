package com.example.fitbuddy.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.fit_buddy.model.PoseModel
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.*

class PoseRepo(context: Context) {

    private val poseDetector: PoseLandmarker

    // Counts
    private var squatCount = 0
    private var pushUpCount = 0
    private var jumpingJackCount = 0
    private var mountainClimberCount = 0
    private var lungeCount = 0
    private var plankCount = 0

    // States
    private var squatState = SquatState.STANDING
    private var pushUpState = PushUpState.UP
    private var jackOpen = false
    private var climberLeftIn = false
    private var climberRightIn = false
    private var lungeState = LungeState.STANDING
    private var plankActive = false

    enum class SquatState { STANDING, DOWN }
    enum class PushUpState { UP, DOWN }
    enum class LungeState { STANDING, DOWN }

    init {
        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(
                BaseOptions.builder()
                    .setModelAssetPath("pose_landmarker_heavy.task")
                    .build()
            )
            .setNumPoses(1)
            .setMinPoseDetectionConfidence(0.6f)
            .setMinTrackingConfidence(0.6f)
            .setMinPosePresenceConfidence(0.6f)
            .build()

        poseDetector = PoseLandmarker.createFromOptions(context, options)
    }

    // ==================== HELPERS ====================

    private fun isValid(lm: NormalizedLandmark): Boolean {
        val x = lm.x()
        val y = lm.y()
        val z = lm.z()
        return x in -0.15f..1.15f && y in -0.15f..1.15f && z.isFinite()
    }

    private fun isSideView(
        lHip: NormalizedLandmark, rHip: NormalizedLandmark,
        lShoulder: NormalizedLandmark, rShoulder: NormalizedLandmark
    ): Boolean {
        val hipDist = abs(lHip.x() - rHip.x())
        val shoulderDist = abs(lShoulder.x() - rShoulder.x())
        val avgDist = (hipDist + shoulderDist) / 2f
        return avgDist < 0.12f
    }

    private fun isFrontView(
        lHip: NormalizedLandmark, rHip: NormalizedLandmark,
        lShoulder: NormalizedLandmark, rShoulder: NormalizedLandmark
    ): Boolean {
        val hipDist = abs(lHip.x() - rHip.x())
        val shoulderDist = abs(lShoulder.x() - rShoulder.x())
        val avgDist = (hipDist + shoulderDist) / 2f
        return avgDist > 0.15f
    }

    private fun angle(a: NormalizedLandmark, b: NormalizedLandmark, c: NormalizedLandmark): Double {
        val abx = (a.x() - b.x()).toDouble()
        val aby = (a.y() - b.y()).toDouble()
        val cbx = (c.x() - b.x()).toDouble()
        val cby = (c.y() - b.y()).toDouble()

        val dot = abx * cbx + aby * cby
        val mag1 = hypot(abx, aby)
        val mag2 = hypot(cbx, cby)

        if (mag1 == 0.0 || mag2 == 0.0) return 180.0

        return Math.toDegrees(acos((dot / (mag1 * mag2)).coerceIn(-1.0, 1.0)))
    }

    private fun noHumanDetected(result: PoseLandmarkerResult): Boolean {
        if (result.landmarks().isEmpty()) return true
        return result.landmarks().get(0).count { isValid(it) } < 18
    }

    // ===================== 🦵 SQUAT =====================
    fun detectSquat(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Stand sideways")

        val lm = result.landmarks().get(0)
        val lHip = lm[23]; val rHip = lm[24]
        val lKnee = lm[25]; val rKnee = lm[26]
        val lAnkle = lm[27]; val rAnkle = lm[28]
        val lShoulder = lm[11]; val rShoulder = lm[12]

        val required = listOf(lHip, rHip, lKnee, rKnee, lAnkle, rAnkle, lShoulder, rShoulder)
        if (required.count { isValid(it) } < 6) return PoseModel("Incorrect", 0, "Show full body")

        if (!isSideView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Turn sideways")

        val kneeAngle = min(angle(lHip, lKnee, lAnkle), angle(rHip, rKnee, rAnkle))

        when (squatState) {
            SquatState.STANDING -> if (kneeAngle < 120.0) squatState = SquatState.DOWN
            SquatState.DOWN -> if (kneeAngle > 160.0) {
                squatCount++
                squatState = SquatState.STANDING
                return PoseModel("Correct", kneeAngle.toInt(), "Rep $squatCount - Good form!")
            }
        }

        val feedback = if (squatState == SquatState.DOWN) "Push up!" else "Lower down!"
        return PoseModel("Correct", kneeAngle.toInt(), feedback)
    }

    // ===================== 💪 PUSH UP =====================
    fun detectPushUp(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Plank sideways")

        val lm = result.landmarks().get(0)
        val lShoulder = lm[11]; val rShoulder = lm[12]
        val lElbow = lm[13]; val rElbow = lm[14]
        val lWrist = lm[15]; val rWrist = lm[16]
        val lHip = lm[23]; val rHip = lm[24]
        val lAnkle = lm[27]; val rAnkle = lm[28]

        val required = listOf(lShoulder, rShoulder, lElbow, rElbow, lWrist, rWrist, lHip, rHip, lAnkle, rAnkle)
        if (required.count { isValid(it) } < 8) return PoseModel("Incorrect", 0, "Show full body")

        if (!isSideView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Turn sideways")

        val elbowAngle = min(angle(lShoulder, lElbow, lWrist), angle(rShoulder, rElbow, rWrist))
        val bodyAngle = min(angle(lShoulder, lHip, lAnkle), angle(rShoulder, rHip, rAnkle))

        if (bodyAngle < 140.0) return PoseModel("Incorrect", elbowAngle.toInt(), "Straighten body")

        when (pushUpState) {
            PushUpState.UP -> if (elbowAngle < 110.0) pushUpState = PushUpState.DOWN
            PushUpState.DOWN -> if (elbowAngle > 160.0) {
                pushUpCount++
                pushUpState = PushUpState.UP
                return PoseModel("Correct", elbowAngle.toInt(), "Rep $pushUpCount - Strong!")
            }
        }

        val feedback = if (pushUpState == PushUpState.UP) "Lower down" else "Push up"
        return PoseModel("Correct", elbowAngle.toInt(), feedback)
    }

    // ===================== ⭐ JUMPING JACK =====================
    fun detectJumpingJack(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Face camera")

        val lm = result.landmarks().get(0)
        val lAnkle = lm[27]; val rAnkle = lm[28]
        val lWrist = lm[15]; val rWrist = lm[16]
        val lShoulder = lm[11]; val rShoulder = lm[12]
        val nose = lm[0]
        val lHip = lm[23]; val rHip = lm[24]

        val required = listOf(lAnkle, rAnkle, lWrist, rWrist, lShoulder, rShoulder, nose, lHip, rHip)
        if (required.count { isValid(it) } < 7) return PoseModel("Incorrect", 0, "Show full body")

        if (!isFrontView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Face camera")

        val bodyWidth = maxOf(abs(lShoulder.x() - rShoulder.x()), abs(lHip.x() - rHip.x()), 0.1f)
        val legDistNorm = abs(lAnkle.x() - rAnkle.x()) / bodyWidth
        val legsOpen = legDistNorm > 1.1f
        val legsClosed = legDistNorm < 0.85f

        val armsUp = lWrist.y() < nose.y() - 0.02f && rWrist.y() < nose.y() - 0.02f
        val armsDown = lWrist.y() > lShoulder.y() + 0.05f && rWrist.y() > rShoulder.y() + 0.05f

        if (!jackOpen && legsOpen && armsUp) {
            jackOpen = true
            return PoseModel("Correct", 0, "Open wide")
        }

        if (jackOpen && legsClosed && armsDown) {
            jackOpen = false
            jumpingJackCount++
            return PoseModel("Correct", 0, "Rep $jumpingJackCount - Nice!")
        }

        val feedback = if (jackOpen) "Close it" else "Jump out"
        return PoseModel("Correct", 0, feedback)
    }

    // ===================== 🏃 MOUNTAIN CLIMBER =====================
    fun detectMountainClimber(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Plank sideways")

        val lm = result.landmarks().get(0)
        val lHip = lm[23]; val rHip = lm[24]
        val lKnee = lm[25]; val rKnee = lm[26]
        val lShoulder = lm[11]; val rShoulder = lm[12]

        val required = listOf(lHip, rHip, lKnee, rKnee, lShoulder, rShoulder)
        if (required.count { isValid(it) } < 6) return PoseModel("Incorrect", 0, "Show full body")

        if (!isSideView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Turn sideways")

        val leftKneeIn = lKnee.y() < lHip.y() - 0.06f
        val rightKneeIn = rKnee.y() < rHip.y() - 0.06f

        var feedback = "Drive knees"

        if (leftKneeIn && !climberLeftIn) {
            climberLeftIn = true
            mountainClimberCount++
            feedback = "Rep $mountainClimberCount"
        }
        if (rightKneeIn && !climberRightIn) {
            climberRightIn = true
            mountainClimberCount++
            feedback = "Rep $mountainClimberCount"
        }

        if (!leftKneeIn) climberLeftIn = false
        if (!rightKneeIn) climberRightIn = false

        return PoseModel("Correct", mountainClimberCount, feedback)
    }

    // ===================== 🏃 LUNGE =====================
    fun detectLunge(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Stand sideways")

        val lm = result.landmarks().get(0)
        val lHip = lm[23]; val rHip = lm[24]
        val lKnee = lm[25]; val rKnee = lm[26]
        val lAnkle = lm[27]; val rAnkle = lm[28]
        val lShoulder = lm[11]; val rShoulder = lm[12]

        val required = listOf(lHip, rHip, lKnee, rKnee, lAnkle, rAnkle, lShoulder, rShoulder)
        if (required.count { isValid(it) } < 6) return PoseModel("Incorrect", 0, "Show full body")

        if (!isSideView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Turn sideways")

        val frontKneeAngle = min(angle(lHip, lKnee, lAnkle), angle(rHip, rKnee, rAnkle))

        when (lungeState) {
            LungeState.STANDING -> if (frontKneeAngle < 110.0) lungeState = LungeState.DOWN
            LungeState.DOWN -> if (frontKneeAngle > 160.0) {
                lungeCount++
                lungeState = LungeState.STANDING
                return PoseModel("Correct", frontKneeAngle.toInt(), "Rep $lungeCount")
            }
        }

        val feedback = if (lungeState == LungeState.DOWN) "Step back up" else "Lunge forward"
        return PoseModel("Correct", frontKneeAngle.toInt(), feedback)
    }

    // ===================== ⏱️ PLANK =====================
    fun detectPlank(bitmap: Bitmap): PoseModel {
        val result = poseDetector.detect(BitmapImageBuilder(bitmap).build())
        if (noHumanDetected(result)) return PoseModel("Ready", 0, "Plank sideways")

        val lm = result.landmarks().get(0)
        val lShoulder = lm[11]; val rShoulder = lm[12]
        val lHip = lm[23]; val rHip = lm[24]
        val lAnkle = lm[27]; val rAnkle = lm[28]

        val required = listOf(lShoulder, rShoulder, lHip, rHip, lAnkle, rAnkle)
        if (required.count { isValid(it) } < 5) return PoseModel("Incorrect", 0, "Show full body")

        if (!isSideView(lHip, rHip, lShoulder, rShoulder)) return PoseModel("Incorrect", 0, "Turn sideways")

        val bodyAngle = min(angle(lShoulder, lHip, lAnkle), angle(rShoulder, rHip, rAnkle))

        if (bodyAngle > 170.0) {
            if (!plankActive) plankActive = true
            return PoseModel("Correct", 0, "Hold strong!")
        } else {
            if (plankActive) {
                plankActive = false
                plankCount++
                return PoseModel("Correct", 0, "Hold complete! Rest")
            }
        }

        return PoseModel("Correct", 0, "Get into plank")
    }

    // ===================== GETTERS & RESET =====================
    fun getSquatCount() = squatCount
    fun getPushUpCount() = pushUpCount
    fun getJumpingJackCount() = jumpingJackCount
    fun getMountainClimberCount() = mountainClimberCount
    fun getLungeCount() = lungeCount
    fun getPlankCount() = plankCount

    fun resetAll() {
        squatCount = 0
        pushUpCount = 0
        jumpingJackCount = 0
        mountainClimberCount = 0
        lungeCount = 0
        plankCount = 0
        squatState = SquatState.STANDING
        pushUpState = PushUpState.UP
        jackOpen = false
        climberLeftIn = false
        climberRightIn = false
        lungeState = LungeState.STANDING
        plankActive = false
    }
}