package com.example.fit_buddy.util

import android.content.Context
import androidx.work.*
import com.example.fit_buddy.workers.DailyReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyReminder(context: Context) {
    val currentTime = Calendar.getInstance()
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    // If target time already passed today → schedule for tomorrow
    if (currentTime.after(targetTime)) targetTime.add(Calendar.DAY_OF_YEAR, 1)

    val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
