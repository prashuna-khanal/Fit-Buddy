package com.example.fit_buddy.model
import com.example.fit_buddy.R

fun samplePosts(): List<FeedPost> {
    return listOf(
        FeedPost(
            username = "Anmy Shrestha",
            profilePic = R.drawable.gympost,
            postImage = R.drawable.gympost,
            caption = "FitBuddy helped me stay consistent! 💪🔥",
            time = "1h ago",
            likes = 125,
            comments = 34
        ),
        FeedPost(
            username = "Sam Adams",
            profilePic = R.drawable.gympost,
            postImage = R.drawable.gympost,
            caption = "Crushed my morning workout 🏋️‍♂️",
            time = "3h ago",
            likes = 98,
            comments = 12
        ),
        FeedPost(
            username = "Maya Karki",
            profilePic = R.drawable.gympost,
            postImage = R.drawable.gympost,
            caption = "Healthy eating + training = results 💜",
            time = "6h ago",
            likes = 160,
            comments = 27
        )
    )
}