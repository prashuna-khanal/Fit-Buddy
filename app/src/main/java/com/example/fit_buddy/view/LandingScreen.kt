package com.example.fit_buddy.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit_buddy.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(navController: NavController, viewModel: UserViewModel) {
    val isLoading by viewModel.loading.observeAsState(false)

    Scaffold(
        containerColor = Color.White,
        bottomBar = { WhyChooseFitBuddySlider() }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //
            Text(
                text = "Get Started",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                ),
                modifier = Modifier.padding(top = 40.dp, bottom = 30.dp)
            )

            // 4 grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item { AuthCard("Sign In", "Already have an account? Sign in to continue", Icons.Default.Person) { navController.navigate("signin") } }
                item { AuthCard("Sign Up", "New here? Create your account today", Icons.Default.Add) { navController.navigate("signup") } }
                item { AuthCard("Reset Password", "Forgot your password? Reset it here", Icons.Default.Refresh) { navController.navigate("reset") } }
                item { AuthCard("OTP", "Verify your account with OTP", Icons.Default.CheckCircle) { navController.navigate("otp") } }
            }

            // loading default for now
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp), color = Color(0xFF6200EE))
            }
        }
    }
}

@Composable
fun WhyChooseFitBuddySlider() {
    val items = listOf("Track Calories", "AI Coaching", "Achieve Goals")
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2)

    // auto scroll after 3sec
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            listState.animateScrollToItem(listState.firstVisibleItemIndex + 1)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Why Choose Fit Buddy?", fontWeight = FontWeight.SemiBold, color = Color.Gray)

        LazyRow(state = listState, userScrollEnabled = false, modifier = Modifier.height(40.dp)) {
            items(Int.MAX_VALUE) { index ->
                val item = items[index % items.size]
                Box(modifier = Modifier.fillParentMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = item, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
                }
            }
        }
    }
}
@Composable
fun AuthCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(4.dp)
            .clickable { onClick() }, //navigation
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F4FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6200EE),
                modifier = Modifier
                    .size(32.dp)
                    .weight(0.2f)
            )

            Spacer(modifier = Modifier.width(12.dp))


            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}