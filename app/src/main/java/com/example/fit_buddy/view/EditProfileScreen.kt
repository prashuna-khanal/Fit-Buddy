package com.example.fit_buddy.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_buddy.R
import com.example.fit_buddy.ui.theme.lavender400


@Composable
fun EditProfileScreenComposable() {

    var name by remember { mutableStateOf("SAM") }
    var email by remember { mutableStateOf("sam@gmail.com") }
    var weight by remember{mutableStateOf("50kg")}
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        //  HEADER

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(lavender400)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Spacer to balance the back icon
                Spacer(modifier = Modifier.width(28.dp))
            }
        }


        // PROFILE IMAGE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Picture",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // FULL NAME
        ProfileInputField(
            label = "FULL NAME",
            value = name,
            onValueChange = { name = it }
        )

        // EMAIL
        ProfileInputField(
            label = "EMAIL ID",
            value = email,
            onValueChange = { email = it }
        )

        // WEIGHT
        ProfileInputField(
            label = "WEIGHT",
            value = weight,
            onValueChange = { weight = it }
        )

        //  PASSWORD WITH EYE ICON
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "PASSWORD",
                fontSize = 13.sp,
                color = Color(0xFF9E9E9E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible)
                                R.drawable.baseline_visibility_24
                            else
                                R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = "Toggle Password",
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .size(22.dp)
                    )
                },
                shape = RoundedCornerShape(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // UPDATE BUTTON
        Button(
            onClick = { /* Save profile */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Text(
                text = "Update",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {

        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF9E9E9E)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
    }
}
@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreenComposable()
}
