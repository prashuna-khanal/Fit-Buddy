package com.example.fit_buddy.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import com.example.fit_buddy.model.FeedPost
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.textMuted
import com.example.fit_buddy.ui.theme.textPrimary
import com.example.fit_buddy.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedSection(
    userViewModel: UserViewModel,
    viewModel: com.example.fit_buddy.viewmodel.FeedViewModel,
    onRequestsClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onFriendsListClick: () -> Unit
) {

    val context = LocalContext.current
    //  state from ViewModel
    val friendsPosts by viewModel.friendsPosts.observeAsState(emptyList())
    val myPosts by viewModel.myPosts.observeAsState(emptyList())
    val allUsers by viewModel.allUsers.observeAsState(emptyList())
    val requests by viewModel.friendRequests.observeAsState(emptyList())

    // UI state
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var showCaptionScreen by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    val buttonLavender = Color(0xFFD9C8F9)

    // search logic doesnot include logged in user
    val filteredUsers = remember(searchQuery, allUsers) {
        if (searchQuery.length >= 1) {
            allUsers.filter {
                it.fullName.contains(searchQuery, ignoreCase = true) &&
                        it.userId != viewModel.currentUserId
            }
        } else emptyList()
    }

    // image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            showCaptionScreen = true
        }
    }

    // caption entry
    if (showCaptionScreen && selectedImageUri != null) {
        CaptionEntryScreen(
            uri = selectedImageUri!!,
            onBack = {
                showCaptionScreen = false
                selectedImageUri = null
            },
            onUpload = { finalCaption ->
                //  handles its own currentUserId internally
                viewModel.sharePost(selectedImageUri!!, finalCaption)
                showCaptionScreen = false
                selectedImageUri = null
                selectedFilter = "My Post" // navigate to MyPost to see the upload
            }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundLightLavender),
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.baseline_arrow_back_ios_24),null)
                        }
                    },
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search users...") },
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            Text("Feed", style = TextStyle(fontWeight = FontWeight.SemiBold), fontSize = 24.sp)
                        }
                    },

                    actions = {
                        IconButton(onClick = {
                            isSearching = !isSearching
                            if (!isSearching) searchQuery = ""
                        }) {
                            Icon(painterResource(if (isSearching) R.drawable.outline_arrow_downward_24 else R.drawable.outline_search_24), null)
                        }
                        IconButton(onClick = { launcher.launch("image/*") }) {
                            Icon(Icons.Default.Add, contentDescription = "Post")
                        }
                        IconButton(onClick = onRequestsClick) {
                            BadgedBox(badge = { if (requests.isNotEmpty()) Badge { Text(requests.size.toString()) } }) {
                                Icon(painterResource(R.drawable.outline_contacts_product_24), null)
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).background(backgroundLightLavender)) {

                if (isSearching && searchQuery.length >= 2) {
                    // search result list
                    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
                        items(filteredUsers) { user ->
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    onProfileClick(user.userId)
                                    isSearching = false
                                    searchQuery = ""
                                }.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(

                                    model = user.profileImage?.takeIf { it.isNotEmpty() } ?: R.drawable.outline_contacts_product_24,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.outline_contacts_product_24),
                                    error = painterResource(R.drawable.outline_contacts_product_24)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(user.fullName, fontWeight = FontWeight.Medium, color = Color.Black)

                            }
                        }
                    }
                } else {
                    // filtering in All POst vs mypost
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("All", "My Post").forEach { filter ->
                            Button(
                                onClick = { selectedFilter = filter },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedFilter == filter) Color(0xFF6200EE) else buttonLavender
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(filter, color = if (selectedFilter == filter) Color.White else textPrimary)
                            }
                        }
                    }


                    val displayPosts = if (selectedFilter == "All") friendsPosts else myPosts

                    if (displayPosts.isEmpty()) {
                        EmptyStateUI(isAll = (selectedFilter == "All")) { isSearching = true }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(displayPosts, key = { it.id }) { post ->
                                FeedCard(
                                    post = post,
                                    currentUserId = viewModel.currentUserId,
                                    allUsers=allUsers,
                                    currentUserProfilePic = userViewModel.user.value?.profileImage,
                                    onUserClick = { onProfileClick(post.userId) },
                                    onLikeClick = { viewModel.toggleLike(post.id) },
                                    onDeleteClick = if (selectedFilter == "My Post") {
                                        { viewModel.deletePost(post.id) }
                                    } else null
                                )
                            }
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
    currentUserId: String,
    allUsers: List<com.example.fit_buddy.model.UserModel>,
    currentUserProfilePic:String?,
    onUserClick: () -> Unit,
    onLikeClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    val liveUser = allUsers.find { it.userId == post.userId }
//    val displayPic = liveUser?.profileImage ?: post.getSafeProfilePic()
    val displayPic=liveUser?.profileImage
    ?: (if(post.userId==currentUserId) currentUserProfilePic else null)
    ?: post.getSafeProfilePic()
    val displayName = liveUser?.fullName ?: post.username
    val isLikedByMe = post.likedBy.containsKey(currentUserId)


    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {

            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp).clickable { onUserClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = displayPic,
//                    model = post.getSafeProfilePic().ifEmpty { R.drawable.outline_contacts_product_24 },
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.LightGray),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.outline_contacts_product_24)
                )
                Spacer(Modifier.width(10.dp))
                Text(text = displayName, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Spacer(Modifier.weight(1f))

                if (onDeleteClick != null) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                    }
                }
            }

//            posted image
            AsyncImage(
                model = post.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(350.dp).background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.outline_contacts_product_24)
            )


            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
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
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun EmptyStateUI(isAll: Boolean, onFindFriends: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painterResource(R.drawable.outline_contacts_product_24), null, Modifier.size(64.dp), Color.Gray)
            Spacer(Modifier.height(16.dp))
            Text(if (isAll) "Your feed is empty." else "You haven't posted yet.", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            if (isAll) {
                Text("Connect with friends to see updates!", textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
                Spacer(Modifier.height(20.dp))
                Button(onClick = onFindFriends) { Text("Find Friends") }
            }
        }
    }
}