package com.example.fit_buddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.AppNotification
import com.example.fit_buddy.repository.NotificationRepo
import com.example.fit_buddy.util.UserSession.currentUserName
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NotificationViewModel(
    private val repo: NotificationRepo,
    private val userId: String
) : ViewModel() {

    // Real-time notifications
    val notifications = repo.getNotifications(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun markAsRead(notificationId: String) {
        repo.markAsRead(userId, notificationId)
    }

    // Helper to send a friend request notification
    fun sendFriendRequestNotification(friendId: String, currentUserName: String) {
        val notif = AppNotification(
            userId = friendId,
            title = "Friend Request",
            message = "$currentUserName sent you a friend request",
            type = "FRIEND_REQUEST"
        )
        repo.sendNotification(notif)
    }
    fun sendFriendAcceptedNotification(receiverUserId: String) {
        val notif = AppNotification(
            userId = receiverUserId,
            title = "Friend Request Accepted!",
            message = "$currentUserName accepted your request. Let's train together!",
            type = "FRIEND_ACCEPTED"
        )
        repo.sendNotification(notif)
    }

}
