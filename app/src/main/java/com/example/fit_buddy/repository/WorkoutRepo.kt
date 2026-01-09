package com.example.fit_buddy.repository

import com.example.fit_buddy.model.WorkoutDay
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun addWorkoutMinutes(minutes: Int)
    fun getWorkoutHistory(): Flow<List<WorkoutDay>>
    fun getCurrentUserId(): String?
}