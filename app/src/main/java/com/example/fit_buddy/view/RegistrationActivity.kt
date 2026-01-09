package com.example.fit_buddy.view

import android.R
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.fit_buddy.model.UserModel
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

@Composable
fun RegistrationBody() {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(false) }


    var userViewModel= remember { UserViewModel(UserRepoImpl()) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var terms by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    var selectedDate by remember { mutableStateOf("") }

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
                var selectedGender by remember { mutableStateOf("") }
                val options = listOf("Female", "Male", "Other")

                Box {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        placeholder = { Text("Select Gender") },
                        enabled = false, // IMPORTANT
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            disabledBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {

                                        selectedGender = it
                                        gender = it
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

                val context = LocalContext.current
                val calendar = Calendar.getInstance()

                OutlinedTextField(
                    value = dob,
                    onValueChange = {},
                    placeholder = { Text("mm/dd/yyyy") },
                    enabled = false, // IMPORTANT
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu_my_calendar),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    dob = "${m + 1}/$d/$y"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
            }

            // =================== PASSWORD ===================
            LabeledField(label = "Password") {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Create a strong password") },
                    visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton (onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(com.example.fit_buddy.R.drawable.baseline_visibility_off_24)
                                else
                                    painterResource(
                                        com.example.fit_buddy.R.drawable.baseline_visibility_24
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

            // =================== CONFIRM PASSWORD ===================
            LabeledField(label = "Confirm Password") {
                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    placeholder = { Text("Re-enter your password") },
                    visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton (onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(com.example.fit_buddy.R.drawable.baseline_visibility_off_24)
                                else
                                    painterResource(
                                        com.example.fit_buddy.R.drawable.baseline_visibility_24
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

            Spacer(modifier = Modifier.height(20.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Checkbox(
                    checked = terms,
                    onCheckedChange ={data ->
                        terms=data
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor= Blue,
                        checkmarkColor = White,
                    )
                )
                Text("I agree to terms & Condition")
            }
            Spacer(modifier = Modifier.height(20.dp))


            // =================== CREATE ACCOUNT BUTTON ===================
            Button(
                onClick = { if (!terms){
                    Toast.makeText(context,"Please agree to terms & conditions", Toast.LENGTH_SHORT).show()
                }
                else {
                    userViewModel.register(email,password){
                            success,message, userId->
                        if (success){
                            var model= UserModel(
                                userId,
                                fullName = fullName,
                                email=email,
                                gender = gender ,
                                password= password,
                                dob = dob
                            )
                            userViewModel.addUserTODatabase(userId, model) { success, msg ->
                                if (success) {
                                    activity?.finish()
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                                }
                            }
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }}},
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