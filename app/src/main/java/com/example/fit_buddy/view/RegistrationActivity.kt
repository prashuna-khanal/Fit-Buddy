package com.example.fit_buddy.view

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
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

@Composable
fun RegistrationBody() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

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

            // App Logo
            Text(
                "FitBuddy",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
            )

            Text(
                "Start your fitness journey",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // =================== FULL NAME ===================
            LabeledField(label = "Full Name") {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Enter your name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // =================== EMAIL ===================
            LabeledField(label = "Email") {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("your@email.com") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            /// =================== GENDER ===================
            LabeledField(label = "Gender") {

                var expanded by remember { mutableStateOf(false) }
                var selectedGender by remember { mutableStateOf("Select Gender") }
                var textFieldSize by remember { mutableStateOf(Size.Zero) }
                val options = listOf("Female", "Male", "Other")

                Box {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        readOnly = true,              // <-- FIXED (keeps outline)
                        placeholder = { Text("Select Gender") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textFieldSize = coordinates.size.toSize()
                            }
                            .clickable { expanded = true },
                        shape = RoundedCornerShape(12.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(
                            with(LocalDensity.current) { textFieldSize.width.toDp() }
                        )
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



            Spacer(modifier = Modifier.height(10.dp))

            // =================== DATE OF BIRTH ===================
            LabeledField(label = "Date of Birth") {

                var selectedDate by remember { mutableStateOf("") }
                val context = LocalContext.current

                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("dd/mm/yyyy") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)

                            DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    selectedDate = "$d/${m + 1}/$y"
                                },
                                year,
                                month,
                                day
                            ).show()
                        },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu_my_calendar),
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }


            // =================== PASSWORD ===================
            LabeledField(label = "Password") {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Create a strong password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // =================== CONFIRM PASSWORD ===================
            LabeledField(label = "Confirm Password") {
                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    placeholder = { Text("Re-enter your password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =================== CREATE ACCOUNT BUTTON ===================
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF4D79), Color(0xFF8A2BE2))
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Create Account",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account?")
                Text(
                    " Log In",
                    color = Color(0xFFFF006E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}
@Composable
fun LabeledField(label: String, content: @Composable () -> Unit) {
    Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(6.dp))
    content()
    Spacer(modifier = Modifier.height(14.dp))

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RegistrationBody()
}