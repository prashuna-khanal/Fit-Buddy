package com.example.fit_buddy.model

data class AppNotification(
    val id: String = "",
    val userId: String = "",        // receiver
    val title: String = "",
    val message: String = "",
    val type: String = "",          // FRIEND_REQUEST, LIKE, STREAK, etc
    val relatedId: String = "",     // postId, userId, achievementId
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
