package com.example.fit_buddy.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.model.AppNotification
import com.example.fit_buddy.ui.theme.lavender100
import com.example.fit_buddy.viewmodel.NotificationViewModel


@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel
) {
    val notifications by viewModel.notifications.collectAsState()


    if (notifications.isEmpty()) {
        Text("No notifications yet", modifier = Modifier.padding(20.dp))
    } else {
        Column {
            notifications.forEach { notif ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            viewModel.markAsRead(notif.id)
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(notif.title, fontWeight = FontWeight.Bold)
                        Text(notif.message)

                        if (!notif.isRead) {
                            Text("NEW", color = Color.Red, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
