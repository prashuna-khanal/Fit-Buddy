package com.example.fit_buddy.view

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_buddy.R
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.repository.UserRepoImpl
import com.example.fit_buddy.viewmodel.UserViewModel
import java.util.Calendar

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationBody()
        }
    }
}

// Factory
class UserViewModelFactory(
    private val application: Application,
    private val repository: UserRepo
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun RegistrationBody() {
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            application = application,
            repository = UserRepoImpl()
        )
    )

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    val activity = context as? Activity

    val gradientBg = Brush.verticalGradient(
        colors = listOf(Color(0xFFFCE4EC), Color(0xFFF3E5F5))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBg)
            .padding(20.dp)
            .border(3.dp, Color.Black, RoundedCornerShape(30.dp))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("FitBuddy", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))

            Text(
                "Start your fitness journey",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            LabeledField("Full Name") {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Enter your name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            LabeledField("Email") {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("your@email.com") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            LabeledField("Gender") {
                var expanded by remember { mutableStateOf(false) }
                val options = listOf("Female", "Male", "Other", "Prefer not to say")

                Box {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        placeholder = { Text("Select Gender") },
                        enabled = false,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            disabledBorderColor = Color.Black
                        )
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedGender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            LabeledField("Date of Birth") {
                val calendar = Calendar.getInstance()
                OutlinedTextField(
                    value = dob,
                    onValueChange = {},
                    placeholder = { Text("mm/dd/yyyy") },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_calendar_today_24),
                            contentDescription = "Select date"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, y, m, d -> dob = "${m + 1}/$d/$y" },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Black
                    )
                )
            }

            LabeledField("Password") {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Create a strong password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.baseline_visibility_24
                                    else R.drawable.baseline_visibility_off_24
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            LabeledField("Confirm Password") {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Re-enter your password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.baseline_visibility_24
                                    else R.drawable.baseline_visibility_off_24
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Blue,
                        checkmarkColor = Color.White
                    )
                )
                Text("I agree to terms & conditions")
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (!termsAccepted) {
                        Toast.makeText(context, "Please agree to terms & conditions", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (fullName.trim().isEmpty() || email.trim().isEmpty() || !email.contains("@") ||
                        password.length < 6 || password != confirmPassword ||
                        selectedGender.isEmpty() || dob.isEmpty()
                    ) {
                        Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    userViewModel.register(email.trim(), password) { success, message, userId ->
                        if (success) {
                            val model = UserModel(
                                userId = userId,
                                fullName = fullName.trim(),
                                email = email.trim(),
                                dob = dob,
                                gender = selectedGender
                            )
                            userViewModel.addUserToDatabase(userId, model) { dbSuccess, dbMsg ->
                                if (dbSuccess) {
                                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                    activity?.finish()
                                } else {
                                    Toast.makeText(context, dbMsg, Toast.LENGTH_SHORT).show()
                                }
                            }
                            //                            else {
//                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFFFF4D79), Color(0xFF8A2BE2))),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Create Account", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Already have an account?")
                Text(
                    " Log In",
                    color = Color(0xFFFF006E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { /* TODO: navigate to login */ }
                )
            }
        }
    }
}

@Composable
fun LabeledField(label: String, content: @Composable () -> Unit) {
    Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(6.dp))
    content()
    Spacer(Modifier.height(14.dp))
}