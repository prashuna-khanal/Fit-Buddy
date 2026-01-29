package com.example.fit_buddy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fit_buddy.view.LandingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpToSignIn {

    @get:Rule
    val composeRule = createAndroidComposeRule<LandingActivity>()

    @Test
    fun landingToSignUp_completesRegistration_andReturnsToLogin() {

        composeRule.onNodeWithText("Sign Up", useUnmergedTree = true).performClick()

        composeRule.onNodeWithTag("fullName").assertIsDisplayed()

        composeRule.onNodeWithTag("fullName").performTextInput("Jane Doe")
        composeRule.onNodeWithTag("email").performTextInput("jane@test.com")
        composeRule.onNodeWithTag("password").performTextInput("Pass1234!")
        composeRule.onNodeWithTag("confirmPassword").performTextInput("Pass1234!")

        composeRule.onNodeWithTag("height").performScrollTo().performTextInput("1.75")

        composeRule.onNodeWithTag("terms").performClick()
        composeRule.onNodeWithTag("register").performClick()

        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithTag("login").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Welcome back").assertIsDisplayed()
    }

    @Test
    fun landingToSignIn_displaysLoginFields() {
        composeRule.onNodeWithText("Sign In", useUnmergedTree = true).performClick()

        composeRule.onNodeWithTag("email").assertIsDisplayed()
        composeRule.onNodeWithTag("password").assertIsDisplayed()
        composeRule.onNodeWithTag("login").assertIsDisplayed()
    }
}