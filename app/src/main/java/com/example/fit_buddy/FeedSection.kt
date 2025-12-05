//package com.example.fit_buddy
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion.Blue
//import androidx.compose.ui.graphics.Color.Companion.White
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.fit_buddy.ui.theme.Black
//import com.example.fit_buddy.ui.theme.backgroundLightLavender
//import com.example.fit_buddy.ui.theme.textMuted
//import com.example.fit_buddy.ui.theme.textPrimary
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FeedSection() {
////dummy posts
//    val posts = remember { samplePosts() }
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    titleContentColor = Black,
//                    actionIconContentColor = Black,
//                    navigationIconContentColor = Black,
//                    containerColor = backgroundLightLavender
//                ),
//                navigationIcon = {
//                    IconButton(onClick = {}) {
//                        Icon(
//                            painter = painterResource(R.drawable.backbutton),
//                            contentDescription = null,
//
//                            )
//                    }
//                },
//                title = {
//                    Text("Feed")
//                },
//                actions = {
//                    IconButton(onClick = {}) {
//                        Icon(
//                            painter = painterResource(R.drawable.friendrequest),
//                            contentDescription = null
//                        )
//                    }
//
//
//                }
//            )
//
//        }
//    ) { padding ->
////        for mypost and friends
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(backgroundLightLavender)
//        ) {
////            buttons
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            )
//            {
//                Button(
//                    onClick = { },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("My Posts", color = textPrimary)
//                }
//                Button(
//                    onClick = { /* Friends action */ },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Friends", color = textPrimary)
//                }
//
//            }
//        }
////vertically scrollable
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(backgroundLightLavender),
//            verticalArrangement = Arrangement.spacedBy(18.dp),
//            contentPadding = PaddingValues(bottom = 24.dp)
//        ) {
//
//
//            // All posts scroll automatically
//            items(posts) { post ->
//                FeedCard(post)
//            }
//        }
//    }
//}
//
//
//// ==========================================================
//// TOP HEADER (Back + 2 Buttons)
//// ==========================================================
//@Composable
//fun TopHeaderSection() {
//
//    Column(modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 16.dp)) {
//
//        // Back Button Row
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = textPrimary,
//                modifier = Modifier
//                    .size(26.dp)
//                    .clickable { }
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Text("Feed", fontSize = 22.sp, color = textPrimary)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Two Buttons: My Posts + Friends
//        Row(modifier = Modifier.fillMaxWidth()) {
//
//            Button(
//                onClick = {},
//                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("My Posts", color = textPrimary)
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Button(
//                onClick = {},
//                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Friends", color = textPrimary)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//    }
//}
//
//// ==========================================================
//// INSTAGRAM STYLE FEED CARD
//// ==========================================================
//@Composable
//fun FeedCard(post: FeedPost) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(Color.White)
//            .padding(16.dp)
//    ) {
//
//        // top user row
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(
//                painter = painterResource(id = post.profilePic),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(45.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column {
//                Text(post.username, fontSize = 16.sp, color = textPrimary)
//                Text(post.time, fontSize = 12.sp, color = textMuted)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // caption
//        Text(post.caption, fontSize = 14.sp, color = textPrimary)
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // post image full width
//        Image(
//            painter = painterResource(id = post.postImage),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp)
//                .clip(RoundedCornerShape(16.dp)),
//            contentScale = ContentScale.Crop
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // likes + comments
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = "Like",
//                tint = Color.Red,
//                modifier = Modifier.size(24.dp)
//            )
//
//            Spacer(modifier = Modifier.width(6.dp))
//
//            Text("${post.likes} Likes", color = textPrimary)
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Text("${post.comments} Comments", color = textPrimary)
//        }
//    }
//}
//
//// ==========================================================
//// DATA MODEL
//// ==========================================================
//data class FeedPost(
//    val username: String,
//    val profilePic: Int,
//    val postImage: Int,
//    val caption: String,
//    val time: String,
//    val likes: Int,
//    val comments: Int
//)
//
//// ==========================================================
//// SAMPLE POSTS (FOR TESTING)
//// ==========================================================
//fun samplePosts(): List<FeedPost> {
//    return listOf(
//        FeedPost(
//            "Anmy Shrestha",
//            R.drawable.gympost,
//            R.drawable.gympost,
//            "FitBuddy helped me stay consistent! 💪🔥",
//            "1h ago",
//            125,
//            34
//        ),
//        FeedPost(
//            "Sam Adams",
//            R.drawable.gympost,
//            R.drawable.gympost,
//            "Crushed my morning workout 🏋️‍♂️",
//            "3h ago",
//            98,
//            12
//        ),
//        FeedPost(
//            "Maya Karki",
//            R.drawable.gympost,
//            R.drawable.gympost,
//            "Healthy eating + training = results 💜",
//            "6h ago",
//            160,
//            27
//        )
//    )
//}
package com.example.fit_buddy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.ui.theme.Black
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.textMuted
import com.example.fit_buddy.ui.theme.textPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedSection(myUsername: String ="Me") {
    val posts = remember { samplePosts() }
//    button color of feed all, friends and mypost
    val buttonLavender = Color(0xFFD9C8F9)
//    state varibale so that known when which feed is selected
    var selectedFilter by remember {mutableStateOf(("All"))}
//    tracking which button is being displayed
    var showMyPostsScreen by remember { mutableStateOf(false) }
//    ifshowMyPostScreen is true, show that instead of freidns posts
    if (showMyPostsScreen) {
//        filyer and only show post belongs to the user
        val myPosts = posts.filter { it.username == myUsername }

        MyPostsScreen(myPosts = myPosts,
            onBack = {
                showMyPostsScreen=false
            })



    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Black,
                    actionIconContentColor = Black,
                    navigationIconContentColor = Black,
                    containerColor = backgroundLightLavender
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.backbutton),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("Feed", style = TextStyle(fontWeight = FontWeight.SemiBold), fontSize = 24.sp)
                        },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.friendrequest),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundLightLavender)
        ) {
            // Buttons row below AppBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { selectedFilter ="All"},
                    colors = ButtonDefaults.buttonColors(containerColor = buttonLavender),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("All", color = if(selectedFilter =="All")Color.Blue else textPrimary,
                        fontWeight = if(selectedFilter =="All") FontWeight.Bold else FontWeight.Normal)

                }

                Button(
                    onClick = { selectedFilter = "My Post"
//                        navigation made
                        showMyPostsScreen = true
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonLavender),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("My Post", color = if (selectedFilter == "My Post") Color.Blue else textPrimary,
                        fontWeight = if (selectedFilter == "My Post") FontWeight.Bold else FontWeight.Normal)
                }
            }

            // Feed posts
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(posts) { post ->
                    FeedCard(post)
                }
            }
        }
    }
}

// ====================== Feed Card ======================
@Composable
fun FeedCard(post: FeedPost) {
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
            Icon(
                painter = painterResource(R.drawable.chat_1947247),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(24.dp).padding(horizontal = 3.dp)
            )
            Text("${post.comments} Comments", color = textPrimary)
        }
    }
}

// ====================== Data Model ======================
data class FeedPost(
    val username: String,
    val profilePic: Int,
    val postImage: Int,
    val caption: String,
    val time: String,
    val likes: Int,
    val comments: Int
)

// ====================== Sample Posts ======================
fun samplePosts(): List<FeedPost> {
    return listOf(
        FeedPost(
            "Anmy Shrestha",
            R.drawable.gympost,
            R.drawable.gympost,
            "FitBuddy helped me stay consistent! 💪🔥",
            "1h ago",
            125,
            34
        ),
        FeedPost(
            "Sam Adams",
            R.drawable.gympost,
            R.drawable.gympost,
            "Crushed my morning workout 🏋️‍♂️",
            "3h ago",
            98,
            12
        ),
        FeedPost(
            "Maya Karki",
            R.drawable.gympost,
            R.drawable.gympost,
            "Healthy eating + training = results 💜",
            "6h ago",
            160,
            27
        )
    )
}

