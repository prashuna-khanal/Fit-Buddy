package com.example.fit_buddy.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fit_buddy.R
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.util.UserSession
import com.example.fit_buddy.util.scheduleDailyReminder
import com.example.fit_buddy.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.example.fit_buddy.utils.NotificationUtils

@Composable
fun SignUpScreen(navController: NavController, viewModel: UserViewModel) {
    val context = LocalContext.current

    // Input state variables
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("Select Date of Birth") }
    var weight by remember { mutableStateOf("70") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("") }
    var isTermsAccepted by remember { mutableStateOf(false) }
    var isBackPressed by remember { mutableStateOf(false) }

    // Height validation state
    var heightError by remember { mutableStateOf<String?>(null) }

    // UI variables
    var passVisible by remember { mutableStateOf(false) }
    var confirmPassVisible by remember { mutableStateOf(false) }
    var isWeightMenuExpanded by remember { mutableStateOf(false) }

    val primaryPurple = Color(0xFF7949D0)
    val bgColor = Color(0xFFF7F5FB)
    val lightLavenderBg = Color(0xFFF8F4FF)
    val buttonLavender = Color(0xFF7949D0)

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
                .height(280.dp)  // slightly taller than before
        ) {

            Image(
                painter = painterResource(id = R.drawable.signup),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 1.15f   // less zoom
                        scaleY = 1.15f
                        translationY = -15f
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
                            startY = 250f
                        )
                    )
            )

            // Create Account Text
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 22.dp, bottom = 0.dp)
            ) {
                Text(
                    "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )

            }
        }
        Spacer(Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .offset(y = (-20).dp) // match the fade offset
        ) {

            // ───────────── FORM FIELDS ─────────────
            CustomLabel("Full Name")
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth().testTag("fullName"),
                placeholder = { Text("Full Name") },
                shape = RoundedCornerShape(18.dp),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryPurple) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(12.dp))

            CustomLabel("Email")
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth().testTag("email"),
                placeholder = { Text("demo@email.com") },
                shape = RoundedCornerShape(18.dp),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = primaryPurple) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(12.dp))

            CustomLabel("Password")
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth().testTag("password"),
                shape = RoundedCornerShape(18.dp),
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            painter = if (passVisible) painterResource(R.drawable.baseline_visibility_24)
                            else painterResource(R.drawable.outline_visibility_off_24),
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

            CustomLabel("Confirm Password")
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth().testTag("confirmPassword"),
                shape = RoundedCornerShape(18.dp),
                visualTransformation = if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPassVisible = !confirmPassVisible }) {
                        Icon(
                            painter = if (confirmPassVisible) painterResource(R.drawable.baseline_visibility_24)
                            else painterResource(R.drawable.outline_visibility_off_24),
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

            Spacer(Modifier.height(16.dp))

            // ───────────── DOB ─────────────
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, day -> dob = "$day/${month + 1}/$year" },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            CustomLabel("Date of Birth")
            OutlinedCard(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth().testTag("dob"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = lightLavenderBg)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = primaryPurple)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = dob,
                        color = if (dob.contains("Select")) Color.Gray else Color.Black
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ───────────── OTHER FIELDS (Gender, Weight, Height) ─────────────
            CustomLabel("Gender")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "Male", onClick = { gender = "Male" }, colors = RadioButtonDefaults.colors(primaryPurple))
                Text("Male", modifier = Modifier.padding(end = 16.dp))
                RadioButton(selected = gender == "Female", onClick = { gender = "Female" }, colors = RadioButtonDefaults.colors(primaryPurple))
                Text("Female")
            }

            Spacer(Modifier.height(16.dp))

            CustomLabel("Weight (kg)")
            Box {
                OutlinedCard(
                    onClick = { isWeightMenuExpanded = true },
                    modifier = Modifier.fillMaxWidth().testTag("weight"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = lightLavenderBg)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$weight kg")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = isWeightMenuExpanded,
                    onDismissRequest = { isWeightMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f).heightIn(max = 300.dp)
                ) {
                    (30..150).forEach { w ->
                        DropdownMenuItem(
                            text = { Text(w.toString()) },
                            onClick = {
                                weight = w.toString()
                                isWeightMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            CustomLabel("Height (m)")
            OutlinedTextField(
                value = height,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' }.let {
                        if (it.count { c -> c == '.' } > 1) it.dropLast(1) else it
                    }
                    height = filtered.take(4)
                    val heightValue = filtered.toFloatOrNull()
                    heightError = when {
                        filtered.isEmpty() -> "Height is required"
                        heightValue == null -> "Enter a valid number"
                        heightValue < 1.0f -> "Height too low (min 1.0 m)"
                        heightValue > 2.5f -> "Height too high (max 2.5 m)"
                        else -> null
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("height"),
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                isError = heightError != null,
                supportingText = {
                    heightError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isTermsAccepted,
                    onCheckedChange = { isTermsAccepted = it },
                    modifier = Modifier.testTag("terms"),
                    colors = CheckboxDefaults.colors(primaryPurple)
                )
                Text("I agree to terms and conditions", fontSize = 14.sp)
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    val heightValue = height.toFloatOrNull()
                    if (heightValue == null || heightValue < 1.0f || heightValue > 2.5f) {
                        Toast.makeText(context, "Please enter valid height (1.0 – 2.5 m)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!isTermsAccepted) {
                        Toast.makeText(context, "Please accept the terms", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.register(email, password) { success, message, userId ->
                        if (success) {
                            val userModel = UserModel(userId, fullName, email, dob, gender, weight, height)
                            viewModel.addUserToDatabase(userId, userModel) { dbSuccess, _ ->
                                if (dbSuccess) {
                                    UserSession.currentUserId = userId
                                    UserSession.currentUserName = fullName.trim()
                                    NotificationUtils.sendWelcomeNotification(userId, fullName)
                                    scheduleDailyReminder(context)
                                    FirebaseAuth.getInstance().signOut()
                                    Toast.makeText(context, "Account created! Please sign in.", Toast.LENGTH_LONG).show()
                                    navController.navigate("signin") { popUpTo("signup") { inclusive = true } }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally).testTag("register"),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(buttonLavender)
            ) {
                Text("Sign Up", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}


@Composable
fun CustomLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 6.dp, top = 4.dp)
    )
}