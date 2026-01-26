package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.*

@Composable
fun PrivacySecurityScreenComposable(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
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
                tint = lavender400,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Privacy & Security",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Privacy & Security Options
        PrivacyItem(
            icon = R.drawable.baseline_lock_24,
            title = "Change Password",
            description = "Update your password regularly to keep your account secure."
        )

        // Two-Factor is still commented out in your original

        PrivacyItem(
            icon = R.drawable.baseline_visibility_24,
            title = "App Permissions",
            description = "Manage which data and device features Fit Buddy can access, like location, notifications, and health data."
        )

        PrivacyItem(
            icon = R.drawable.baseline_privacy_tip_24,
            title = "Privacy Settings",
            description = "Control how your personal information and activity data are used and shared."
        )

        PrivacyItem(
            icon = R.drawable.baseline_logout_24,
            title = "Logout from Devices",
            description = "Sign out from other devices where your account is currently active."
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun PrivacyItem(
    icon: Int,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(lavender100, RoundedCornerShape(16.dp))
            .clickable { /* TODO: add navigation or action */ }
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = lavender400,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = textSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacySecurityComposablePreview() {
    PrivacySecurityScreenComposable()
}