package com.example.fit_buddy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEE6FF),
            Color(0xFFD7C9FF),
            Color(0xFFC3B6F5)
        )
    )

    val primaryPurple = Color(0xFF6200EE)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { WhyChooseFitBuddySlider() }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {

            // Centered Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // ✅ center cards vertically
            ) {

                Text(
                    text = "Get Started",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryPurple
                    ),
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                // Grid Cards
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { AuthCard("Sign In", "Already have an account? Sign in to continue", Icons.Default.Person) { navController.navigate("signin") } }
                    item { AuthCard("Sign Up", "New here? Create your account today", Icons.Default.Add) { navController.navigate("signup") } }
                    item(span = { GridItemSpan(2) }) { AuthCard("Reset Password", "Forgot your password? Reset it here", Icons.Default.Refresh) { navController.navigate("reset") } }
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 20.dp),
                        color = primaryPurple
                    )
                }
            }
        }
    }
}

@Composable
fun WhyChooseFitBuddySlider() {
    val items = listOf("Track Calories", "AI Coaching", "Achieve Goals")
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2)
    val primaryPurple = Color(0xFF6200EE)

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            listState.animateScrollToItem(listState.firstVisibleItemIndex + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Why Choose Fit Buddy?", fontWeight = FontWeight.SemiBold, color = Color.Gray)

        LazyRow(
            state = listState,
            userScrollEnabled = false,
            modifier = Modifier.height(40.dp)
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = items[index % items.size]
                Box(
                    modifier = Modifier.fillParentMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item, fontWeight = FontWeight.Bold, color = primaryPurple)
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
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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
