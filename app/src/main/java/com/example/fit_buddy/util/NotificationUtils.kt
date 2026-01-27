package com.example.fit_buddy.utils

import com.example.fit_buddy.model.AppNotification
import com.example.fit_buddy.repository.NotificationRepoImpl

object NotificationUtils {

    fun sendWelcomeNotification(userId: String, fullName: String) {
        val repo = NotificationRepoImpl()
        val notif = AppNotification(
            userId = userId,
            title = "Welcome to Fit Buddy! 💪",
            message = "Hi $fullName! Your fitness journey starts now. Complete your profile and start tracking workouts.",
            type = "WELCOME",
            relatedId = "",
            isRead = false,
            timestamp = System.currentTimeMillis()
        )
        repo.sendNotification(notif)
    }

    fun sendFriendRequestNotification(
        receiverUserId: String,
        senderName: String,
        senderUserId: String
    ) {
        val repo = NotificationRepoImpl()
        val notif = AppNotification(
            userId = receiverUserId,
            title = "New Friend Request",
            message = "$senderName wants to be your fitness buddy!",
            type = "FRIEND_REQUEST",
            relatedId = senderUserId,
            isRead = false,
            timestamp = System.currentTimeMillis()
        )
        repo.sendNotification(notif)
    }

    fun sendFriendAcceptedNotification(
        receiverUserId: String, // original requester
        acceptorName: String
    ) {
        val repo = NotificationRepoImpl()
        val notif = AppNotification(
            userId = receiverUserId,
            title = "Friend Request Accepted!",
            message = "$acceptorName accepted your request. Let's train together!",
            type = "FRIEND_ACCEPTED",
            relatedId = "",
            isRead = false,
            timestamp = System.currentTimeMillis()
        )
        repo.sendNotification(notif)
    }

}