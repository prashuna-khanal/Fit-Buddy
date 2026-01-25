import com.example.fit_buddy.model.FeedPost

fun samplePosts(): List<FeedPost> {
    val currentTime = System.currentTimeMillis()

    return listOf(
        FeedPost(
            id = "1",
            userId = "sample_user_1",
            username = "Anmy Shrestha",
            imageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?auto=format&fit=crop&q=80&w=1000",
            caption = "FitBuddy helped me stay consistent! 💪🔥 Day 30 of my challenge.",
            timestamp = currentTime - (3600000 * 1),
            likedBy = mapOf("user_a" to true),
            profilePic = "",
            comments = 34
        ),
        FeedPost(
            id = "2",
            userId = "sample_user_2",
            username = "Sam Adams",
            imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&q=80&w=1000",
            caption = "Crushed my morning workout! New PR on bench press today. 🏋️‍♂️",
            timestamp = currentTime - (3600000 * 3),
            likedBy = emptyMap(),
            profilePic = "",
            comments = 12
        ),
        FeedPost(
            id = "3",
            userId = "sample_user_3",
            username = "Maya Karki",
            imageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?auto=format&fit=crop&q=80&w=1000",
            caption = "Healthy eating + consistent training = results you can see. 💜",
            timestamp = currentTime - (3600000 * 6),
            likedBy = emptyMap(),
            profilePic = "",
            comments = 27
        ),
        FeedPost(
            id = "4",
            userId = "sample_user_4",
            username = "David Miller",
            imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?auto=format&fit=crop&q=80&w=1000",
            caption = "Sunday Runday! 🏃‍♂️ 10km done and dusted. Feeling great.",
            timestamp = currentTime - (86400000),
            likedBy = emptyMap(),
            profilePic = "",
            comments = 5
        )
    )
}