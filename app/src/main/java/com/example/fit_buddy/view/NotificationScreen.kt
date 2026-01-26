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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.model.AppNotification
import com.example.fit_buddy.ui.theme.lavender100



@Composable
fun NotificationScreen(
    notification: AppNotification?,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = notification != null,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        notification?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable { onDismiss() },
                colors = CardDefaults.cardColors(containerColor = lavender100)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(it.title, fontWeight = FontWeight.Bold)
                    Text(it.message, fontSize = 14.sp)
                }
            }
        }
    }
}
//@Preview
//@Composable
//fun NotificationScreenPreview() {
//    NotificationScreen()
//}

