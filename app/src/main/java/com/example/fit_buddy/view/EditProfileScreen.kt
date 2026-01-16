package com.example.fit_buddy.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fit_buddy.R
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.ui.theme.lavender400
import com.example.fit_buddy.viewmodel.UserViewModel


@Composable
fun EditProfileScreenComposable(
    viewModel: UserViewModel = viewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val userId = viewModel.getCurrentUserId()

    var name by remember { mutableStateOf("SAM") }
    var email by remember { mutableStateOf("sam@gmail.com") }
    var weight by remember{mutableStateOf("50kg")}
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }

    val user by viewModel
        .getUserData(userId ?: "")
        .collectAsState(initial = null)

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.getUserData(userId).collect { user ->
                user?.let {
                    name = it.fullName
                    email = it.email
                    weight = it.weight
                }
            }
        }
    }
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
            ) { uri ->
        uri?.let {
            selectedImageUri = it
            viewModel.uploadProfileImage(it) { success, msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }


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
                painter = rememberAsyncImagePainter(
                    model = selectedImageUri ?: user?.profileImage
                ),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Picture",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.clickable {
                    imagePickerLauncher.launch("image/*")
                }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            // UPDATE BUTTON
            Button(
                onClick = {
                    if (userId == null) return@Button

                    val updatedUser = UserModel(
                        userId = userId,
                        fullName = name,
                        email = email,
                        weight = weight
                    )

                    viewModel.updateUserProfile(userId, updatedUser) { success, msg ->
                        // Toast / Snackbar if needed
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Update")
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
        onClick = { showDeleteDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Red
        )
    ) {
        Text("Delete Account", fontWeight = FontWeight.Bold)
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Account")
            },
            text = {
                Text(
                    "Are you sure you want to permanently delete your account? " +
                            "This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false

                        val userId = viewModel.getCurrentUserId() ?: return@TextButton

                        viewModel.deleteAccount(userId) { success, msg ->
                            if (success) {
                                viewModel.logout()
                                // 👉 navigate to Login screen here
                            }
                        }
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
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
//@Preview
//@Composable
//fun EditProfileScreenPreview() {
//    EditProfileScreenComposable(viewModel: UserViewModel)
//}
