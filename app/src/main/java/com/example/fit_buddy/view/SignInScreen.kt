package com.example.fit_buddy.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit_buddy.R
import com.example.fit_buddy.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel) {

    // User input states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val primaryPurple = Color(0xFF6200EE)

    // Gradient background (matches splash)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEE6FF),
            Color(0xFFD7C9FF),
            Color(0xFFC3B6F5)
        )
    )

    // Scroll state for keyboard + small screens
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(gradientBackground)
            .padding(bottom = 16.dp) // avoid cut-off on small screens
    ) {

        // 🌊 Top wave header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFC3B6F5),
                            Color(0xFFD7C9FF)
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(40.dp)
                    .background(primaryPurple, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
        }

        // 🏋️‍♀️ App Branding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fit Buddy",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                text = "Train smarter. Live stronger 💪",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        // 📦 Login Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {

            Text(
                text = "Sign in",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            // 📧 Email field
            Text("Email", color = Color.Gray, fontSize = 14.sp)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("demo@email.com") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = primaryPurple) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(16.dp))

            // 🔐 Password field
            Text("Password", color = Color.Gray, fontSize = 14.sp)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your password") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = primaryPurple) },
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passVisible) R.drawable.baseline_visibility_24
                                else R.drawable.outline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = primaryPurple
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(12.dp))

            // ☑ Remember + Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )
                    Text("Remember Me", fontSize = 12.sp)
                }

                Text(
                    "Forgot Password?",
                    color = primaryPurple,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { navController.navigate("forgot") }
                )
            }

            Spacer(Modifier.height(24.dp))

            // 🚀 Login Button with empty field check
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.login(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, WorkoutActivity::class.java))
                            (context as? android.app.Activity)?.finish()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryPurple)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(18.dp))

            // ➕ Signup row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    "Sign up",
                    color = primaryPurple,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.navigate("signup") }
                )
            }
        }
    }
}
