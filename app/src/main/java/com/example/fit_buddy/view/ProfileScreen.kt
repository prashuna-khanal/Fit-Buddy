package com.example.fit_buddy.view

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.ui.theme.lavender500
import com.example.fit_buddy.viewmodel.FeedViewModel
import com.example.fit_buddy.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(viewModel: FeedViewModel,userViewModel: UserViewModel = viewModel()) {

    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
    ) {
        when (selectedIndex) {
            0 -> ProfileMainScreen (userViewModel){ selectedIndex = it }
            1 -> EditProfileScreenComposable (
                viewModel=userViewModel,
                onBackClick = { selectedIndex = 0 }   )
            2 -> FriendListScreen(
                viewModel = viewModel,
                onBack = { selectedIndex = 0 },
                onFriendClick = { friendId ->
                    // Navigate to friend's profile if you have that screen
                    // println("Clicked on friend: $friendId")
                }
            )//
//            3 -> PrivacySecurityScreenComposable (onBackClick = { selectedIndex = 0 })
            4 -> AppSettingsScreenComposable (onBackClick = { selectedIndex = 0 })
            5 -> HelpSupportScreenComposable(onBackClick = { selectedIndex = 0 })
        }
    }
}

@Composable
fun ProfileMainScreen(
    userViewModel: UserViewModel,
    onNavigate: (Int) -> Unit
) {
    val user by userViewModel.user.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Spacer(Modifier.height(20.dp))

        Text(
            "Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2EEFF), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if(user?.profileImage.isNullOrEmpty()) R.drawable.img else user?.profileImage
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    user?.fullName ?: "Loading...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60469D)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    user?.email ?: "email@gmail.com",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Member since January 2024",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        ProfileItem(R.drawable.baseline_person_24, "Edit Profile") { onNavigate(1) }
        ProfileItem(R.drawable.baseline_people_24, "Friends") { onNavigate(2) }
        ProfileItem(R.drawable.baseline_settings_24, "App Settings") { onNavigate(4) }
        ProfileItem(R.drawable.baseline_help_24, "Help & Support") { onNavigate(5) }

        Spacer(Modifier.height(20.dp))

        LogoutItem {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }
}

@Composable
fun ProfileItem(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF8F5FF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = lavender500
        )

        Spacer(Modifier.width(16.dp))

        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.Gray
        )
    }
}

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF2EEFF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_logout_24),
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = Color(0xFFD00000)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            "Logout",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD00000)
        )
    }
}
