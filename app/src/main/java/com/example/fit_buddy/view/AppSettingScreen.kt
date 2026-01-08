package com.example.fit_buddy.view
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
fun AppSettingsScreenComposable() {

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var metricUnitsEnabled by remember { mutableStateOf(true) }
    var analyticsEnabled by remember { mutableStateOf(true) }

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
                    .clickable { }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "App Settings",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // SETTINGS OPTIONS
        SettingsItemSwitch(
            icon = R.drawable.baseline_notifications_24,
            title = "Enable Notifications",
            description = "Receive reminders for workouts, water intake, and activity goals.",
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

//        SettingsItemSwitch(
//            icon = R.drawable.baseline_dark_mode_24,
//            title = "Dark Mode",
//            description = "Reduce eye strain by enabling dark theme.",
//            checked = darkModeEnabled,
//            onCheckedChange = { darkModeEnabled = it }
//        )

        SettingsItemSwitch(
            icon = R.drawable.baseline_straighten_24,
            title = "Use Metric Units",
            description = "Display weight in kg and distance in kilometers.",
            checked = metricUnitsEnabled,
            onCheckedChange = { metricUnitsEnabled = it }
        )

        SettingsItemSwitch(
            icon = R.drawable.baseline_privacy_tip_24,
            title = "Allow Analytics",
            description = "Help us improve Fit Buddy by sharing anonymous usage data.",
            checked = analyticsEnabled,
            onCheckedChange = { analyticsEnabled = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun SettingsItemSwitch(
    icon: Int,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(lavender100, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = lavender400,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
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

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppSettingsScreenPreview() {
    AppSettingsScreenComposable()
}
