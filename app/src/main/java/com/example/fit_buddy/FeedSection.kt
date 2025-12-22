
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.fit_buddy.ui.theme.Black
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.textMuted
import com.example.fit_buddy.ui.theme.textPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedSection(myUsername: String ="BABITA") {
    val posts = remember { samplePosts() }
//    button color of feed all, friends and mypost
    val buttonLavender = Color(0xFFD9C8F9)
//    state varibale so that known when which feed is selected
    var selectedFilter by remember {mutableStateOf(("All"))}
//    tracking which button is being displayed
    var showMyPostsScreen by remember { mutableStateOf(false) }
//    ifshowMyPostScreen is true, show that instead of freidns posts
// ... inside your FeedSection composable

//    ifshowMyPostScreen is true, show that instead of freidns posts
    if (showMyPostsScreen) {
//        filyer and only show post belongs to the user
        val myPosts = posts.filter { it.username == myUsername }

        //  Pass the 'myPosts' list to the MyPostsScreen composable
        MyPostsScreen(
            posts = myPosts, // Add this line
            onBack = {
                showMyPostsScreen = false
            }
        )
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
                            painter = painterResource(R.drawable.outline_arrow_back_ios_24),
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
                            painter = painterResource(R.drawable.outline_contacts_product_24),
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
                painter = painterResource(R.drawable.talk_10298102),
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

