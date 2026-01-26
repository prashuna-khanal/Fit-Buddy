package com.example.fit_buddy.repository

import com.example.fit_buddy.model.WorkoutDay
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate

class WorkoutRepositoryImpl : WorkoutRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    private fun getWorkoutsRef(userId: String) = db.getReference("users").child(userId).child("workouts")

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override suspend fun addWorkoutMinutes(minutes: Int) {
        val userId = getCurrentUserId() ?: return
        val today = LocalDate.now().toString() // "2026-01-08"

        val workoutsRef = getWorkoutsRef(userId)
        val todayRef = workoutsRef.child(today)

        // Use transaction to safely increment minutes
        todayRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val current = currentData.getValue(WorkoutDay::class.java)
                val newMinutes = (current?.minutes ?: 0) + minutes
                val workoutDay = WorkoutDay(date = today, minutes = newMinutes)
                currentData.value = workoutDay
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                // Optional: handle error
            }
        })
    }

    override fun getWorkoutHistory(): Flow<List<WorkoutDay>> = callbackFlow {
        val userId = getCurrentUserId() ?: run {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val workoutsRef = getWorkoutsRef(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(WorkoutDay::class.java)
                }.sortedByDescending { it.date } // newest first
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        workoutsRef.addValueEventListener(listener)
        awaitClose { workoutsRef.removeEventListener(listener) }
    }
}