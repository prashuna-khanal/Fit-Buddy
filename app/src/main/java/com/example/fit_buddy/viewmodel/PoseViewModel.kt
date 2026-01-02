package com.example.fitbuddy.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fit_buddy.model.PoseModel
import com.example.fitbuddy.repository.PoseRepo

class PoseViewModel(private val repo: PoseRepo) : ViewModel() {

    var poseState by mutableStateOf<PoseModel?>(null)
        private set

    var currentExercise by mutableStateOf(ExerciseType.SQUAT)
        private set

    var squatCount by mutableStateOf(0)
        private set
    var pushUpCount by mutableStateOf(0)
        private set
    var jumpingJackCount by mutableStateOf(0)
        private set
    var mountainClimberCount by mutableStateOf(0)
        private set

    enum class ExerciseType {
        SQUAT,
        PUSH_UP,
        PLANK,
        LUNGE,
        JUMPING_JACK,
        MOUNTAIN_CLIMBER
    }

    fun processFrame(bitmap: Bitmap) {
        poseState = when (currentExercise) {
            ExerciseType.SQUAT -> repo.detectSquat(bitmap)
            ExerciseType.PUSH_UP -> repo.detectPushUp(bitmap)
            ExerciseType.PLANK -> repo.detectPlank(bitmap)
            ExerciseType.LUNGE -> repo.detectLunge(bitmap)
            ExerciseType.JUMPING_JACK -> repo.detectJumpingJack(bitmap)
            ExerciseType.MOUNTAIN_CLIMBER -> repo.detectMountainClimber(bitmap)
        }

        squatCount = repo.getSquatCount()
        pushUpCount = repo.getPushUpCount()
        jumpingJackCount = repo.getJumpingJackCount()
        mountainClimberCount = repo.getMountainClimberCount()
    }

    fun setExerciseType(exercise: ExerciseType) {
        currentExercise = exercise
        poseState = null
        repo.resetAll()
    }
}
