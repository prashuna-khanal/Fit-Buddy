package com.example.fitbuddy.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.fit_buddy.model.PoseModel
import com.example.fitbuddy.repository.PoseRepo

class PoseViewModel(val repo: PoseRepo) : ViewModel() {
    var poseState by mutableStateOf<PoseModel?>(null)
        private set

    var currentExercise by mutableStateOf(ExerciseType.SQUAT)
        private set

    var squatCount by mutableStateOf(0)
        private set

    var pushUpCount by mutableStateOf(0)
        private set

    enum class ExerciseType {
        SQUAT, PUSH_UP
    }

    fun processFrame(bitmap: Bitmap) {
        // This calls detectSquat() or detectPushUp() based on currentExercise
        poseState = when (currentExercise) {
            ExerciseType.SQUAT -> repo.detectSquat(bitmap)
            ExerciseType.PUSH_UP -> repo.detectPushUp(bitmap)
        }
        // Update counts from repo
        squatCount = repo.getSquatCount()
        pushUpCount = repo.getPushUpCount()
    }

    fun setExerciseType(exercise: ExerciseType) {
        currentExercise = exercise
        // Reset the other exercise when switching
        when (exercise) {
            ExerciseType.SQUAT -> repo.resetPushUp()
            ExerciseType.PUSH_UP -> repo.resetSquat()
        }
        poseState = null
    }

    fun resetCounts() {
        repo.resetSquat()
        repo.resetPushUp()
        squatCount = 0
        pushUpCount = 0
    }
}