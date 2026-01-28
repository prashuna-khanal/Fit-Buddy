package com.example.fit_buddy

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.view.SignUpScreen
import com.example.fit_buddy.viewmodel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTesting {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun registrationInstrumentedTesting() {

        // Arrange: mock repo
        val repo = mock(UserRepo::class.java)

        // mock register
        doAnswer {
            val callback = it.getArgument<(Boolean, String, String) -> Unit>(2)
            callback(true, "Register success", "user123")
            null
        }.`when`(repo).register(
            eq("ram@gmail.com"),
            eq("password123"),
            any()
        )

        // mock add user
        doAnswer {
            val callback = it.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Saved")
            null
        }.`when`(repo).addUserToDatabase(
            eq("user123"),
            any(),
            any()
        )

        val viewModel = UserViewModel(
            androidx.test.core.app.ApplicationProvider.getApplicationContext(),
            repo
        )

        composeRule.setContent {
            SignUpScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Fill form
        composeRule.onNodeWithTag("fullName")
            .performTextInput("Ram Sharma")

        composeRule.onNodeWithTag("email")
            .performTextInput("ram@gmail.com")

        composeRule.onNodeWithTag("password")
            .performTextInput("password123")

        composeRule.onNodeWithTag("confirmPassword")
            .performTextInput("password123")

        composeRule.onNodeWithTag("height")
            .performTextInput("1.75")

        // Accept terms
        composeRule.onNodeWithTag("terms")
            .performClick()

        // Click register
        composeRule.onNodeWithTag("register")
            .performClick()
    }
}
