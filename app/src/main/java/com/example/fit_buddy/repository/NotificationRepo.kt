package com.example.fit_buddy.repository

import com.example.fit_buddy.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepo {
        fun sendNotification(notification: AppNotification)
        fun getNotifications(userId: String): Flow<List<AppNotification>>
        fun markAsRead(userId: String, notificationId: String)


}