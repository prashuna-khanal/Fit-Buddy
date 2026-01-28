package com.example.fit_buddy.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val primaryPurple = Color(0xFF7949D0)
    val bgColor = Color(0xFFF7F5FB)
    val placeholderGrey = Color(0xFF8A8989)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
    ) {

        /* ---------------- IMAGE SECTION ---------------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.signin),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 1.28f   // ✅ zoom
                        scaleY = 1.28f
                        translationY = -35f // ✅ remove empty background
                    }
            )

            // subtle bottom fade
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                bgColor.copy(alpha = 0.35f)
                            ),
                            startY = 320f
                        )
                    )
            )
        }

        /* ---------------- CONTENT ---------------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .offset(y = (-28).dp) // ✅ reduced gap
        ) {

            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Train smart. Stay consistent.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(26.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth().testTag("email"),
                placeholder = { Text("demo@gmail.com", color = placeholderGrey) },
                leadingIcon = {
                    Icon(Icons.Default.Email, null, tint = primaryPurple)
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth().testTag("password"),
                placeholder = { Text("••••••••", color = placeholderGrey) },
                visualTransformation =
                    if (passVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, null, tint = primaryPurple)
                },
                trailingIcon = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passVisible)
                                    R.drawable.baseline_visibility_24
                                else
                                    R.drawable.outline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = primaryPurple
                        )
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Text(
                text = "Forgot password?",
                fontSize = 13.sp,
                color = primaryPurple,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp).testTag("Forgot password?")
                    .clickable { navController.navigate("forgot")}
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {
                    viewModel.login(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                            context.startActivity(
                                Intent(context, WorkoutActivity::class.java)
                            )
                            (context as? android.app.Activity)?.finish()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp).testTag("login"),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryPurple)
            ) {
                Text(
                    "LOGIN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("New here? ", color = Color.Gray)
                Text(
                    "Create account",
                    color = primaryPurple,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
