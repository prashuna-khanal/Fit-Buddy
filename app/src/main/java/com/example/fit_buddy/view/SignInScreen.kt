package com.example.fit_buddy.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit_buddy.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel) {
    //  user input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    //   back
    var isBackPressed by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
//        wavy
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(
                    color = Color(0xFFC9B6E4),
                    shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {isBackPressed= true
                navController.popBackStack()
            },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 16.dp)
                    .background(
                        if (isBackPressed) Color(0xFFB19CD9) else Color.Transparent,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription =null,
                    tint = Color.White
                )
            }
//           if we want logo guys

        }

        // form fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {

            Text(
                text = "Sign in",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // email field
            Text("Email", color = Color.Gray, fontSize = 14.sp)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth().testTag("email"),
                placeholder = { Text("demo@email.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF6200EE)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF6200EE)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // password field
            Text("Password", color = Color.Gray, fontSize = 14.sp)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth() .testTag("password"),
                placeholder = { Text("enter your password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6200EE)) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF6200EE)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Remember Me", fontSize = 12.sp)
                }
                Text(
                    text = "Forgot Password?",
                    fontSize = 12.sp,
                    color = Color(0xFF6200EE),
                    modifier = Modifier.clickable {
                        navController.navigate("forgot")
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // button
            Button(
                onClick = {
                    // calls the ViewModel function we built earlier
                    viewModel.login(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
//                            starting activity
                            val intent = Intent(context, WorkoutActivity::class.java)
                            context.startActivity(intent)
//                            close login
                            (context as? android.app.Activity)?.finish()
                            // navigate to dashboard and clear back screens

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp).testTag("login"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB19CD9))
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // if no acc we will go to singup
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an Account? ", color = Color.Gray)
                Text(
                    text = "Sign up",
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("signup") }
                )
            }
        }
    }
}