package com.example.fit_buddy.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.textPrimary
import com.example.fit_buddy.ui.theme.textMuted
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.items
import com.example.fit_buddy.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(posts : List<FeedPost>,
    onBack: () -> Unit = {}) {
    // Dummy data for My Posts
    val myPosts = remember { sampleMyPosts() }
    var selectedPostIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedPostIndex == null) "My Posts" else myPosts[selectedPostIndex!!].username,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedPostIndex != null) {
                            selectedPostIndex = null // back to grid
                        } else {
                            onBack() // exit MyPostsScreen
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundLightLavender)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundLightLavender)
        ) {
            if (selectedPostIndex == null) {
                // Grid view of posts
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(myPosts) { post ->
                        FeedGridItem(post) {
                            selectedPostIndex = myPosts.indexOf(post)
                        }
                    }
                }
            } else {
                // Vertical feed of all posts
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(myPosts) { post ->
                        FeedCardItem(post)
                    }
                }
            }
        }
    }
}

@Composable
fun FeedGridItem(post: FeedPost, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = post.postImage),
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun FeedCardItem(post: FeedPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        // User info row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = post.profilePic),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(post.username, fontSize = 16.sp, color = textPrimary)
                Text(post.time, fontSize = 12.sp, color = textMuted)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Caption
        Text(post.caption, fontSize = 14.sp, color = textPrimary)

        Spacer(modifier = Modifier.height(10.dp))

        // Post image
        Image(
            painter = painterResource(id = post.postImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Likes + comments
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Like",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("${post.likes} Likes", color = textPrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Text("${post.comments} Comments", color = textPrimary)
        }
    }
}

// Dummy Data for MyPostsScreen
fun sampleMyPosts(): List<FeedPost> {
    return listOf(
        FeedPost(
            "BABITA",
            R.drawable.gympost,
            R.drawable.gym1,
            "Workout vibes today! 💪🔥",
            "2h ago",
            120,
            15
        ),
        FeedPost(
            "BABITA",
            R.drawable.gympost,
            R.drawable.gym3,
            "Healthy breakfast = happy day 🍳🥑",
            "5h ago",
            98,
            10
        ),
        FeedPost(
            "BABITA",
            R.drawable.gym22,
            R.drawable.gym22,
            "Evening run completed! 🏃‍♂️💨",
            "8h ago",
            135,
            18
        ),
        FeedPost(
            "BABITA",
            R.drawable.gym3,
            R.drawable.gym1,
            "Leg day gains! 🏋️‍♂️🔥",
            "1d ago",
            200,
            25
        ),
        FeedPost(
            "BABITA",
            R.drawable.gym22,
            R.drawable.gym3,
            "Yoga session for flexibility 🧘‍♂️",
            "2d ago",
            85,
            12
        )
    )
}
