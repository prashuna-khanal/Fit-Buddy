package com.example.fit_buddy.model

data class WorkoutDay(
    val date: String = "",
    val minutes: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "date" to date,
            "minutes" to minutes,
            "timestamp" to timestamp
        )
    }
}