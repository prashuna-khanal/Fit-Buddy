package com.example.fit_buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.ui.theme.backgroundLightLavender


@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()                      // FULL SCREEN like others
            .background(backgroundLightLavender) // SAME BG
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(25.dp))

        // PROFILE CARD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2EEFF), RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text("Sam", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("sam@email.com", fontSize = 14.sp, color = Color.Gray)
                Text("Member since January 2024", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // SETTINGS
        SettingItem(R.drawable.baseline_person_24, "Edit Profile")
        SettingItem(R.drawable.baseline_notifications_24, "Notifications")
        SettingItem(R.drawable.baseline_security_24, "Privacy & Security")
        SettingItem(R.drawable.baseline_settings_24, "App Settings")
        SettingItem(R.drawable.baseline_help_24, "Help & Support")

        Spacer(modifier = Modifier.height(10.dp))

        SettingItemLogout("Logout")
        Spacer(modifier = Modifier.height(40.dp))
    }
}


@Composable
fun SettingItem(icon: Int, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF8F5FF), RoundedCornerShape(16.dp))
            .clickable { }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@Composable
fun SettingItemLogout(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF2EEFF), RoundedCornerShape(16.dp))
            .clickable { }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.baseline_logout_24), // your logout icon
            contentDescription = null,
            tint = Color.Black, // red color
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD00000) // red
        )
    }
}


@Preview
@Composable
fun ProfilePreview(){
    ProfileScreen()
}

