package com.example.fit_buddy.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
    onBack: () -> Unit // navigate back to FeedSection
) {

    val myUsername =viewModel.currentUserId
    // observing real time data from Firebase via ViewModel
    val posts by viewModel.getPostsByUser(myUsername).observeAsState(emptyList())

    // logic for switching between Grid and List
    val selectedPostId by viewModel.selectedPostId

    // local states for the upload draft
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
                title = {
                    // dynamic title based on view mode
                    Text(if (selectedPostId == null) "My Journey" else "Post Detail", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if(viewModel.selectedPostId.value !=null){
                            viewModel.selectPost(null) // return to grid
                        }else{
                            onBack()//exit to feed
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                            contentDescription = "null"
                        )
                    }
                },
                actions = {
                    // only show upload button in Grid mode
                    if (selectedPostId == null) {
                        IconButton(onClick = { viewModel.showUploadSection = !viewModel.showUploadSection }) {
                            Icon(Icons.Default.Add, contentDescription = "null")
                        }
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

            // UI Section
            if (viewModel.showUploadSection && selectedPostId == null) {
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
                                    viewModel.sharePost(it, caption, myUsername)
                                    // reset local state after triggering upload
                                    caption = ""
                                    selectedUri = null
                                }
                            },
                            enabled = selectedUri != null && !viewModel.isUploading
                        ) {
                            if (viewModel.isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Post to Journey")
                            }
                        }
                    }
                }
            }

            // navigation Switch logic: Grid vs Vertical Scroll
            if (selectedPostId == null) {
                // activities of fitbuddy
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
                                .padding(2.dp)
                                .clickable {
                                    // trigeering the switch to vertical Scroll
                                    viewModel.selectPost(post.id)
                                },
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.gympost) // fallback while loading
                        )
                    }
                }
            } else {
                val filteredPosts = posts.filter { it.id == selectedPostId }
                // vertical scroll
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
                ) {
                    items(filteredPosts) { post ->

                        // resusing your existing FeedCard for consistent design
                        FeedCard(post = post, onUserClick = { },
                            currentUserId = viewModel.currentUserId,
                            onLikeClick = { viewModel.toggleLike(post.id) },
                            onDeleteClick = {
                                viewModel.deletePost(post.id)
                            })
                    }
                }
            }
        }
    }
}