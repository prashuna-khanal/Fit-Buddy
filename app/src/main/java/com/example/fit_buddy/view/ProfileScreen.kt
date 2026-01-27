package com.example.fit_buddy.view

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fit_buddy.R
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.ui.theme.backgroundLightLavender
import com.example.fit_buddy.viewmodel.FeedViewModel
import com.example.fit_buddy.viewmodel.UserViewModel

@Composable
fun ProfileScreen(viewModel: FeedViewModel,userViewModel: UserViewModel) {

    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
    ) {
        when (selectedIndex) {
            0 -> ProfileMainScreen (userViewModel = userViewModel,
                onNavigate = { selectedIndex = it })
            1 -> EditProfileScreenComposable (
                onBackClick = { selectedIndex = 0 }   )
//            2 -> FriendListScreen()
            3 -> PrivacySecurityScreenComposable (onBackClick = { selectedIndex = 0 })
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLightLavender)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(Modifier.height(20.dp))

        Text(
            "Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(25.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2EEFF), RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ProfileUserImage(user)

            Spacer(Modifier.width(20.dp))

            ProfileUserColumn(user)
        }


        Spacer(Modifier.height(30.dp))

        ProfileItem(R.drawable.baseline_person_24, "Edit Profile") { onNavigate(1) }
        ProfileItem(R.drawable.baseline_people_24, "Friends") { onNavigate(2) }
        ProfileItem(R.drawable.baseline_security_24, "Privacy & Security") { onNavigate(3) }
        ProfileItem(R.drawable.baseline_settings_24, "App Settings") { onNavigate(4) }
        ProfileItem(R.drawable.baseline_help_24, "Help & Support") { onNavigate(5) }

        Spacer(Modifier.height(20.dp))

        LogoutItem {
            // logout logic
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
            contentDescription = null
        )
    }
}


@Composable
fun ProfileUserColumn(user: com.example.fit_buddy.model.UserModel?) {
    Column {
        Text(
            text = user?.fullName ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = user?.email ?: "",
            fontSize = 14.sp,
            color = Color.Gray
        )
        if (!user?.dob.isNullOrEmpty()) {
            Text(
                text = "DOB: ${user?.dob}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun ProfileUserImage(user: UserModel?) {
    AsyncImage(
        model = user?.profileImage?.takeIf { it.isNotEmpty() }
            ?: R.drawable.img,
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(14.dp)),
        placeholder = painterResource(R.drawable.img),
        error = painterResource(R.drawable.img)
    )
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
}
