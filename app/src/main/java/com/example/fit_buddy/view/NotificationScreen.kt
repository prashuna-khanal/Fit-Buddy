package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.model.AppNotification
import com.example.fit_buddy.ui.theme.lavender100
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import com.example.fit_buddy.viewmodel.NotificationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    onBack: () -> Unit
) {
    // Load notifications for current user when this screen is shown
    val notifications by viewModel.notifications.collectAsState(initial = emptyList())

    // Scaffold with TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = com.example.fit_buddy.R.drawable.outline_arrow_back_ios_24),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notifications yet",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(notifications) { notif ->
                    val isFriendRequest = notif.type == "FRIEND_REQUEST"
                    val background = if (isFriendRequest) lavender100.copy(alpha = 0.5f) else Color.White

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // Mark notification as read in Firebase
                                viewModel.markAsRead(notif.id)
                                // TODO: navigate to profile if friend request
                            },
                        colors = CardDefaults.cardColors(containerColor = background),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = notif.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(notif.message, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = relativeTime(notif.timestamp),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            if (!notif.isRead) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• New", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Format timestamp into "just now / min ago / hr ago / date"
private fun relativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60000L -> "Just now"
        diff < 3600000L -> "${diff / 60000} min ago"
        diff < 86400000L -> "${diff / 3600000} hr ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}
