
package com.example.fit_buddy.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.fit_buddy.model.FriendRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // Required for the + button
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import com.example.fit_buddy.model.FeedPost
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.textMuted
import com.example.fit_buddy.ui.theme.textPrimary
import com.example.fit_buddy.model.samplePosts
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedSection(
    myUsername: String = "BABITA",
    viewModel: com.example.fit_buddy.viewmodel.FeedViewModel,
    onRequestsClick: () -> Unit,
    onProfileClick: (String) -> Unit
) {

    //observe live post
    val livePosts by viewModel.friendsPosts.observeAsState(emptyList())
//    search actions variables
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val allUsers by viewModel.allUsers.observeAsState(emptyList())

// seeing list of friend from repository
    val requests by viewModel.friendRequests.observeAsState(emptyList())
    val dummyPosts = remember { samplePosts() }
    val displayPosts = if(livePosts.isEmpty()) dummyPosts else livePosts
    // State variables
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var showCaptionScreen by remember { mutableStateOf(false) }
    var showMyPostsScreen by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    val posts = remember { samplePosts() }
    val buttonLavender = Color(0xFFD9C8F9)
//    show users only after entering more
    val filteredUsers = if (searchQuery.length >= 1) {
        allUsers.filter {
            it.fullName.contains(searchQuery, ignoreCase = true)
        }
    } else {
        emptyList()
    }

    // Image Picker Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            showCaptionScreen = true
        }
    }

    // navigation logic
    if (showCaptionScreen && selectedImageUri != null) {
        CaptionEntryScreen(
            uri = selectedImageUri!!,
            onBack = {
                showCaptionScreen = false
                selectedImageUri = null
            },
            onUpload = { finalCaption ->
                viewModel.sharePost(selectedImageUri!!, finalCaption, viewModel.currentUserId)
                showCaptionScreen = false
                selectedImageUri = null
                showMyPostsScreen = true
            }
        )
    } else if (showMyPostsScreen) {
        // call  MyPostsScreen.kt file
        MyPostsScreen(
            viewModel = viewModel,
            onBack = {
                showMyPostsScreen = false
                selectedFilter = "All"
            }
        )
    } else {
        // main feed
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundLightLavender
                    ),
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.outline_arrow_back_ios_24), null)
                        }
                    },
                    title = {
                        if (isSearching){
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search users...") },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
                            )


                        }else {
                            Text(
                                "Feed",
                                style = TextStyle(fontWeight = FontWeight.SemiBold),
                                fontSize = 24.sp
                            )
                        }
                    },
                    actions = {
//                        search
//                       search
                        IconButton(onClick = {
                            isSearching = !isSearching
                            if(!isSearching) searchQuery =""
                        }) {
                            Icon(
                                painter = painterResource(id = if(isSearching)R.drawable.outline_arrow_downward_24 else R.drawable.outline_search_24  ),
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                        // + FOR UPLOAD
                        IconButton(onClick = { launcher.launch("image/*") }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Post")
                        }
                        IconButton(onClick = onRequestsClick) {
                            BadgedBox(
                                badge = {
                                    if (requests.isNotEmpty()) {
                                        Badge { Text(requests.size.toString()) }
                                    }
                                }
                            ) {
                                Icon(painterResource(R.drawable.outline_contacts_product_24), null)
                            }
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
//                for search
            if (isSearching && searchQuery.length>=2){
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ){
                    items(filteredUsers) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onProfileClick(user.userId)
                                    isSearching = false
                                    searchQuery = "" //khali for next time
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Profile Placeholder
                            AsyncImage(
                                model = user.profilePicUrl,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray),
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.outline_contacts_product_24)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(user.fullName, fontWeight = FontWeight.Medium, color = Color.Black)
                        }
                    }
                }
                }else
                {


                // filtering Buttons Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { selectedFilter = "All" },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonLavender),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("All", color = if (selectedFilter == "All") Color.Blue else textPrimary)
                    }

                    Button(
                        onClick = {
                            selectedFilter = "My Post"
                            showMyPostsScreen = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonLavender),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("My Post", color = if (selectedFilter == "My Post") Color.Blue else textPrimary)
                    }
                }

                // main feed List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(displayPosts) { post ->
                        FeedCard(
                            post = post,
                            currentUserId = viewModel.currentUserId,
                            onUserClick = { onProfileClick(post.username) },
                            onLikeClick = {
                                viewModel.toggleLike(postId = post.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
}
@Composable
fun FeedCard(
    post: FeedPost,
    currentUserId:String,
    onUserClick: () -> Unit,
    onLikeClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
//    check liked or not
    val isLikedByMe = post.likedBy.containsKey(currentUserId)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            //header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onUserClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray))
                Spacer(Modifier.width(8.dp))
                Text(text = post.username, fontWeight = FontWeight.Bold)

                Spacer(Modifier.weight(1f))

                // delete button only for postscreen
                if (onDeleteClick != null) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete,
                            contentDescription = "Delete Post",
                            tint = Color.Gray
                        )
                    }
                }
            }

            //  image
            AsyncImage(
                model = post.profilePic ?: R.drawable.outline_contacts_product_24,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentScale = ContentScale.Crop
            )

            // like Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = if (isLikedByMe) Color.Red else Color.Gray
                    )
                }
                Text(text = "${post.likesCount} likes", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            // caption
            if (post.caption.isNotEmpty()) {
                Text(
                    text = post.caption,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

