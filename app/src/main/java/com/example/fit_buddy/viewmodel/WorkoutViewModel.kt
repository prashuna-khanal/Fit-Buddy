package com.example.fit_buddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.WorkoutDay
import com.example.fit_buddy.repository.WorkoutRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class WorkoutViewModel : ViewModel() {

    // Create the repository directly inside the ViewModel — no injection needed
    private val workoutRepo = WorkoutRepositoryImpl()

    private val minMinutesForStreak = 15

    val workoutHistory: Flow<List<WorkoutDay>> = workoutRepo.getWorkoutHistory()

    val streakData: Flow<StreakData> = workoutHistory.map { history ->
        val today = LocalDate.now()
        val qualifiedDates = history
            .filter { it.minutes >= minMinutesForStreak }
            .map { LocalDate.parse(it.date) }
            .toSet()

        var currentStreak = 0
        var bestStreak = 0
        var tempStreak = 0

        var date = today
        while (date.year >= today.year - 10) { // safe limit
            if (qualifiedDates.contains(date)) {
                tempStreak++
                bestStreak = maxOf(bestStreak, tempStreak)
                if (currentStreak == 0) currentStreak = tempStreak // only set once from today backward
            } else {
                if (currentStreak > 0) { // stop current streak after first miss
                    // do nothing — current is already set
                }
                tempStreak = 0
            }
            date = date.minusDays(1)
            if (currentStreak > 0 && tempStreak == 0) break // optimization: stop after current streak ends
        }

        // Last 7 days: oldest → newest (e.g., Wed → Tue if today is Tue)
        val last7Days = mutableListOf<Pair<String, Boolean>>()
        for (i in 6 downTo 0) {
            val d = today.minusDays(i.toLong())
            val dayName = d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val minutesToday = history.find { it.date == d.toString() }?.minutes ?: 0
            val qualified = minutesToday >= minMinutesForStreak
            last7Days.add(dayName to qualified)
        }

        StreakData(currentStreak, bestStreak, last7Days)
    }

    val graphData: Flow<GraphData> = workoutHistory.map { history ->
        val today = LocalDate.now()
        val last30Days = (0..29).map { today.minusDays(it.toLong()) }.reversed() // oldest first

        val minutesMap = history.associateBy { it.date }

        val labels = last30Days.map {
            val month = it.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            "$month ${it.dayOfMonth}"
        }

        val minutes = last30Days.map { minutesMap[it.toString()]?.minutes ?: 0 }

        GraphData(labels, minutes)
    }

    // Call this when user completes a workout
    fun addWorkoutMinutes(minutes: Int) {
        if (minutes <= 0) return
        viewModelScope.launch {
            workoutRepo.addWorkoutMinutes(minutes)
        }
    }
    // ====== TODAY'S WORKOUT MINUTES ======
    private val _todayWorkoutMinutes = MutableStateFlow(0)
    val todayWorkoutMinutes: StateFlow<Int> get() = _todayWorkoutMinutes

    init {
        // Whenever workoutHistory updates, recalc today's minutes
        viewModelScope.launch {
            workoutHistory.collect { history ->
                val today = LocalDate.now().toString()
                val totalToday = history.filter { it.date == today }.sumOf { it.minutes }
                _todayWorkoutMinutes.value = totalToday
            }
        }
    }

    // Optional helper: manually refresh today's minutes
    fun refreshTodayMinutes() {
        viewModelScope.launch {
            val history = workoutHistory.first() // get current list
            val today = LocalDate.now().toString()
            _todayWorkoutMinutes.value = history.filter { it.date == today }.sumOf { it.minutes }
        }
    }

    data class StreakData(
        val currentStreak: Int = 0,
        val bestStreak: Int = 0,
        val last7Days: List<Pair<String, Boolean>> = emptyList()
    )

    data class GraphData(
        val dates: List<String> = emptyList(),
        val minutes: List<Int> = emptyList()
    )
}