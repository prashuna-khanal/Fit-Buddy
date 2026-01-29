package com.example.fit_buddy.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.fit_buddy.R
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.ui.theme.lavender400
import com.example.fit_buddy.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditProfileScreenComposable(
    viewModel: UserViewModel = viewModel(),
    onBackClick: () -> Unit = { }   // ← New: parent passes navigation action
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user by viewModel.user.observeAsState()

    val userId = viewModel.getCurrentUserId()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

//    val user by viewModel
//        .getUserData(userId ?: "")
//        .collectAsState(initial = null)

    LaunchedEffect(user) {
        user?.let {
            name = it.fullName
            email = it.email
            weight = it.weight
        }

    }
//    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { localUri ->
            selectedImageUri = localUri

            // upload to Cloudinary
            MediaManager.get().upload(localUri)
                .callback(object : UploadCallback {
                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val cloudinaryUrl = resultData?.get("secure_url").toString()

                        // save that link to Firebase
                        viewModel.updateProfileWithUrl(cloudinaryUrl) { success, msg ->
                            if (success) {
                                selectedImageUri = null
                                Toast.makeText(context, "Photo Synced!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Toast.makeText(context, "Cloudinary Error: ${error?.description}", Toast.LENGTH_LONG).show()
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                    override fun onStart(requestId: String?) {}
                }).dispatch()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(lavender400)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() }   // ← This makes it go back to Profile section
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(120.dp)
                    .shadow(8.dp, CircleShape)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = selectedImageUri ?: user?.profileImage,
                    ),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentScale = ContentScale.Crop
                )

                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Change picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(lavender400)
                        .padding(8.dp)
                        .align(Alignment.BottomEnd)
                        .clickable { imagePickerLauncher.launch("image/*") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Change Picture",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
            )
        }

        Spacer(modifier = Modifier.height(19.dp))

        // INPUT FIELDS - now in card-like containers
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInputField("FULL NAME", name) { name = it }
                Spacer(Modifier.height(16.dp))
                ProfileInputField("EMAIL ID", email) { email = it }
                Spacer(Modifier.height(16.dp))
                ProfileInputField("WEIGHT", weight) { weight = it }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PASSWORD FIELD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.baseline_visibility_24
                                    else R.drawable.baseline_visibility_off_24
                                ),
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))


        Button(
            onClick = {
                if (userId == null || user == null) return@Button

                val hasProfileChanged =
                    name != user!!.fullName ||
                            email != user!!.email ||
                            weight != user!!.weight

                val hasPasswordChanged = password.isNotBlank()

                // Nothing changed
                if (!hasProfileChanged && !hasPasswordChanged) {
                    Toast.makeText(context, "Nothing to update", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // 🔹 Update profile (name/email/weight) if changed
                if (hasProfileChanged) {
                    val updatedUser = user!!.copy(
                        fullName = name,
                        email = email,
                        weight = weight
                    )

                    viewModel.updateUserProfile(userId, updatedUser) { success, msg ->
                        if (!success) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            return@updateUserProfile
                        }

                        // Password may or may not change
                        updatePasswordIfNeeded(context, hasPasswordChanged, password)
                    }
                } else {
                    //  Only password changed
                    updatePasswordIfNeeded(context, hasPasswordChanged, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = lavender400)
        ) {
            Text("Update Profile", color = Color.White, fontSize = 16.sp)
        }



        Spacer(modifier = Modifier.height(24.dp))

        // DELETE ACCOUNT BUTTON
        OutlinedButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Delete Account", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    // DELETE DIALOG (unchanged)
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
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
                                // navigate to Login screen here
                            }
                        }
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
fun updatePasswordIfNeeded(
    context: android.content.Context,
    hasPasswordChanged: Boolean,
    password: String
) {
    if (hasPasswordChanged) {
        FirebaseAuth.getInstance().currentUser
            ?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                Toast.makeText(
                    context,
                    if (task.isSuccessful) "Updated successfully"
                    else task.exception?.message ?: "Update failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    } else {
        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
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