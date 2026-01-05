package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fit_buddy.R
import com.example.fit_buddy.viewmodel.FeedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.ui.layout.ContentScale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherUserProfileScreen(
    userId: String,
    viewModel: com.example.fit_buddy.viewmodel.FeedViewModel,
    onBack: () -> Unit
) {
    // 1. Observe the target user's data and the request status
    val allUsers by viewModel.allUsers.observeAsState(emptyList())
    val user = allUsers.find { it.userId == userId }

    // Status can be: "none", "requested", or "friends"
    val friendshipStatus by viewModel.getFriendshipStatus(userId).observeAsState("none")

    val softLavender = Color(0xFFC9B6E4)
    val buttonLavender = Color(0xFFB19CD9)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(user?.fullName ?: "Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.outline_arrow_back_ios_24), contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Picture Circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(softLavender),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = user?.fullName ?: "Unknown User", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = user?.email ?: "", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    if (friendshipStatus == "none") {
                        viewModel.sendFriendRequest(userId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (friendshipStatus) {
                        "requested" -> Color.LightGray
                        "friends" -> Color(0xFF81C784) // Green for friends
                        else -> buttonLavender
                    }
                ),
                enabled = friendshipStatus == "none" // Disable if already requested or friends
            ) {
                Text(
                    text = when (friendshipStatus) {
                        "requested" -> "Requested"
                        "friends" -> "Friends"
                        else -> "Follow"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Placeholder for User's Posts
            Text("User Posts", modifier = Modifier.padding(start = 16.dp).align(Alignment.Start), fontWeight = FontWeight.SemiBold)
            Divider(modifier = Modifier.padding(16.dp))
            //  can add a LazyVerticalGrid here later to show their photos!
        }
    }
}
