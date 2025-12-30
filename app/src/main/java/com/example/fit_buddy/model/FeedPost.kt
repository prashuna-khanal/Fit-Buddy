package com.example.fit_buddy.model

data class FeedPost(
    val id: String = "",
    val username: String = "",
    val imageUrl: String = "",
    val caption: String = "",
    val timestamp: Long = 0L,
    val likes: Int = 0,
    val profilePic: Any = 0, // to accept String OR Int
    val comments: Int = 0
)