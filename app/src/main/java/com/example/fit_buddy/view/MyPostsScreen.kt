//package com.example.fit_buddy.view

//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
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
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.res.painterResource
//import com.example.fit_buddy.ui.theme.backgroundLightLavender
//import com.example.fit_buddy.ui.theme.textPrimary
//import com.example.fit_buddy.ui.theme.textMuted
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.lazy.items
//import com.example.fit_buddy.R
//import com.example.fit_buddy.model.FeedPost
//@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun MyPostsScreen(posts : List<FeedPost>,
//                  onBack: () -> Unit = {}) {
//    // Dummy data for My Posts
//    val myPosts = remember { sampleMyPosts() }
//    var selectedPostIndex by remember { mutableStateOf<Int?>(null) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = if (selectedPostIndex == null) "My Posts" else myPosts[selectedPostIndex!!].username,
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        if (selectedPostIndex != null) {
//                            selectedPostIndex = null // back to grid
//                        } else {
//                            onBack() // exit MyPostsScreen
//                        }
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textPrimary)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundLightLavender)
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(backgroundLightLavender)
//        ) {
//            if (selectedPostIndex == null) {
//                // Grid view of posts
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(3),
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp),
//                    contentPadding = PaddingValues(4.dp),
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    items(myPosts) { post ->
//                        FeedGridItem(post) {
//                            selectedPostIndex = myPosts.indexOf(post)
//                        }
//                    }
//                }
//            } else {
//                // Vertical feed of all posts
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(18.dp),
//                    contentPadding = PaddingValues(bottom = 24.dp)
//                ) {
//                    items(myPosts) { post ->
//                        FeedCardItem(post)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FeedGridItem(post: FeedPost, onClick: () -> Unit) {
//    Image(
//        painter = painterResource(id = post.postImage),
//        contentDescription = null,
//        modifier = Modifier
//            .aspectRatio(1f)
//            .clip(RoundedCornerShape(8.dp))
//            .clickable { onClick() },
//        contentScale = ContentScale.Crop
//    )
//}
//
//@Composable
//fun FeedCardItem(post: FeedPost) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(Color.White)
//            .padding(16.dp)
//    ) {
//        // User info row
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = painterResource(id = post.profilePic),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(45.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(post.username, fontSize = 16.sp, color = textPrimary)
//                Text(post.time, fontSize = 12.sp, color = textMuted)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Caption
//        Text(post.caption, fontSize = 14.sp, color = textPrimary)
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // Post image
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
//        // Likes + comments
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = "Like",
//                tint = Color.Red,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(6.dp))
//            Text("${post.likes} Likes", color = textPrimary)
//            Spacer(modifier = Modifier.width(12.dp))
//            Text("${post.comments} Comments", color = textPrimary)
//        }
//    }
//}
//
package com.example.fit_buddy.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import com.example.fit_buddy.viewmodel.FeedViewModel
import com.example.fit_buddy.ui.theme.backgroundLightLavender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    viewModel: FeedViewModel,
    onBack: () -> Unit // Navigation back to FeedSection
) {
    // Observing real-time data from Firebase via ViewModel
    val posts by viewModel.allPosts.observeAsState(emptyList())

    // Local states for the upload draft
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var caption by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Journey", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showUploadSection = !viewModel.showUploadSection }) {
                        Icon(Icons.Default.Add, contentDescription = "Toggle Upload")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundLightLavender
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {

            // --- Upload UI Section ---
            if (viewModel.showUploadSection) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundLightLavender.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Select Workout Photo")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        selectedUri?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        OutlinedTextField(
                            value = caption,
                            onValueChange = { caption = it },
                            label = { Text("How was the workout?") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedUri?.let {
                                    viewModel.sharePost(it, caption, "BABITA")
                                    // Reset local state after triggering upload
                                    caption = ""
                                    selectedUri = null
                                }
                            },
                            enabled = selectedUri != null && !viewModel.isUploading
                        ) {
                            if (viewModel.isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp), // Use Modifier to set the size
                                    color = Color.White,
                                    strokeWidth = 2.dp // Optional: makes the line thinner/thicker
                                )
                            } else {
                                Text("Post to Journey")
                            }
                        }
                    }
                }
            }

            //
            Text(
                text = "Recent Activities",
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(2.dp)
            ) {
                items(posts) { post ->
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.gympost) // Fallback while loading
                    )
                }
            }
        }
    }
}