package com.example.fit_buddy.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.backgroundLightLavender
<<<<<<< HEAD

@Composable
fun ProfileScreen() {

=======
import com.example.fit_buddy.viewmodel.FeedViewModel

@Composable
fun ProfileScreen(viewModel: FeedViewModel) {
    val context = LocalContext.current
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
    ) {
        when (selectedIndex) {
<<<<<<< HEAD
            0 -> ProfileMainScreen { selectedIndex = it }
            1 -> EditProfileScreenComposable ()
//            2 -> FriendsScreenComposable ()
//            3 -> NotificationScreenComposable ()
            3 -> PrivacySecurityScreenComposable ()
//            4 -> AppSettingScreenComposable ()
            5 -> HelpSupportScreenComposable()
=======
            // Main Profile Menu
            0 -> ProfileMainScreen(
                onNavigate = { selectedIndex = it },
                onBackClick = {
                    // This takes you back to WorkoutActivity
                    val intent = android.content.Intent(context, WorkoutActivity::class.java)
                    intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                    (context as? android.app.Activity)?.finish()
                }
            )

            // Sub-Screens
            1 -> EditProfileScreenComposable()

            2 -> FriendListScreen(
                viewModel = viewModel,
                onBack = { selectedIndex = 0 },
                onFriendClick = { friendId ->
                    viewModel.navigateToFriendProfile(friendId)
                    selectedIndex = 7
                }
            )

            3 -> PrivacySecurityScreenComposable()

            5 -> HelpSupportScreenComposable()

            // View other user profile
            7 -> OtherUserProfileScreen(
                userId = viewModel.selectedFriendId,
                viewModel = viewModel,
                onBack = { selectedIndex = 2 }
            )

            else -> ProfileMainScreen(onNavigate = { selectedIndex = it }, onBackClick = {})
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
        }
    }
}

@Composable
fun ProfileMainScreen(
<<<<<<< HEAD
    onNavigate: (Int) -> Unit
=======
    onNavigate: (Int) -> Unit,
    onBackClick: () -> Unit
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
<<<<<<< HEAD

        Spacer(Modifier.height(20.dp))

        Text(
            "Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
=======
        Spacer(Modifier.height(20.dp))

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
                    .clickable { onBackClick() }
            )
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2

        Spacer(Modifier.height(25.dp))

<<<<<<< HEAD
=======
            Spacer(modifier = Modifier.width(28.dp)) // Balance the back icon
        }

        Spacer(Modifier.height(25.dp))

        // USER INFO CARD
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
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

            Spacer(Modifier.width(20.dp))

            Column {
                Text("Sam", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("sam@email.com", fontSize = 14.sp, color = Color.Gray)
                Text("Member since January 2024", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(30.dp))

<<<<<<< HEAD
=======
        // MENU ITEMS
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
        ProfileItem(R.drawable.baseline_person_24, "Edit Profile") { onNavigate(1) }
        ProfileItem(R.drawable.baseline_people_24, "Friends") { onNavigate(2) }
        ProfileItem(R.drawable.baseline_security_24, "Privacy & Security") { onNavigate(3) }
        ProfileItem(R.drawable.baseline_settings_24, "App Settings") { onNavigate(4) }
        ProfileItem(R.drawable.baseline_help_24, "Help & Support") { onNavigate(5) }

        Spacer(Modifier.height(20.dp))

        LogoutItem {
<<<<<<< HEAD
            // logout logic
=======
            // Add Logout Logic Here (e.g., FirebaseAuth.getInstance().signOut())
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
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
            .padding(vertical = 10.dp)
            .background(Color(0xFFF8F5FF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )

        Spacer(Modifier.width(18.dp))

        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
<<<<<<< HEAD
            contentDescription = null
=======
            contentDescription = null,
            modifier = Modifier.size(16.dp)
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
        )
    }
}

@Composable
fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(Color(0xFFF2EEFF), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_logout_24),
            contentDescription = null,
<<<<<<< HEAD
=======
            tint = Color(0xFFD00000),
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
            modifier = Modifier.size(26.dp)
        )

        Spacer(Modifier.width(18.dp))

        Text(
            "Logout",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD00000)
        )
    }
<<<<<<< HEAD
}

@Preview
@Composable
fun ProfilePreview(){
    ProfileScreen()
}
=======
}
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2
