package com.example.fit_buddy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fit_buddy.view.LandingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LandingActivity>()

    @Test
    fun testSignUpFlow_completesSuccessfully() {
        composeRule.onNodeWithText("Sign up", ignoreCase = true).performClick()

        composeRule.onNodeWithTag("signup_screen").assertIsDisplayed()

        composeRule.onNodeWithTag("fullname").performTextInput("Test User")
        composeRule.onNodeWithTag("Signup_email").performTextInput("test@example.com")
        composeRule.onNodeWithTag("password").performTextInput("SecurePass123")
        composeRule.onNodeWithTag("confirmP").performTextInput("SecurePass123")


        composeRule.onNodeWithTag("height").performScrollTo().performTextInput("1.75")


        composeRule.onNodeWithTag("terms_checkbox").performScrollTo().performClick()

        composeRule.onNodeWithTag("signup_btn").performScrollTo().performClick()


        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithTag("login_btn").fetchSemanticsNodes().isNotEmpty()
        }
    }
}