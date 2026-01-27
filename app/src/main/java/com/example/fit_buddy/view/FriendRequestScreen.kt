package com.example.fit_buddy.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import com.example.fit_buddy.model.FriendRequest
import com.example.fit_buddy.utils.NotificationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestsScreen(
    requests: List<FriendRequest>,
    onAccept: (String) -> Unit,
    onDelete: (String) -> Unit,
    onProfileClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val confirmLavender = Color(0xFFD9C8F9)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Friend Requests", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.outline_arrow_back_ios_24), contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No new Friend Requests", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(requests) { request ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = request.profilePicUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .clickable { onProfileClick(request.userId) },
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(
                                text = request.username.ifEmpty { "Unknown User" },
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    onAccept(request.userId)
                                    // Send accepted notification to the requester
                                    val acceptorName = "Your Name" // Replace with actual current user name from ViewModel or auth
                                    NotificationUtils.sendFriendAcceptedNotification(
                                        receiverUserId = request.userId, // requester
                                        acceptorName = acceptorName
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = confirmLavender),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Confirm", color = Color.Black, fontSize = 12.sp)
                            }
                            Spacer(Modifier.width(4.dp))
                            TextButton(onClick = { onDelete(request.userId) }) {
                                Text("Delete", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}