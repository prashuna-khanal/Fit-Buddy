package com.example.fit_buddy.model

data class FeedPost(
    val id: String = "",
    val userId: String = "",
    val username: String ="",
    val imageUrl: String = "",
    val caption: String = "",
    val timestamp: Long?=null,
    val likedBy: Map<String, Boolean> = emptyMap(),
    val profilePic: Any? = "",
    val comments: Int = 0
){
//    count unique likes

    val likesCount: Int get() = likedBy.size
    fun getSafeProfilePic(): String {
        return if (profilePic is String) profilePic else ""
    }
}