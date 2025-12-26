package com.example.fit_buddy.model

data class FeedPost(
    val id: String = "",
    val username: String = "",
    val imageUrl: String = "",
    val caption: String = "",
    val timestamp: Long = 0,
    val likes: Int = 0,
    val profilePic: Int = 0,
    val postImage: Int = 0,
    val time: String = "",
    val comments: Int = 0
)