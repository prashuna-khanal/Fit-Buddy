package com.example.fit_buddy

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fit_buddy.view.LoginScreen
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.view.ForgetPasswordScreen
import com.example.fit_buddy.view.WorkoutActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginInstrumentedTesting() {
        // Arrange: Mock UserRepo
        val repo = mock(UserRepo::class.java)

        doAnswer {
            val callback = it.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("ram@gmail.com"), eq("password123"), any())

        // Create real ViewModel with Application + mocked repo
        val viewModel = UserViewModel(
            androidx.test.core.app.ApplicationProvider.getApplicationContext(),
            repo
        )

        // Set the content
        composeRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Type email
        composeRule
            .onNodeWithTag("email")
            .performTextInput("ram@gmail.com")

        // Type password
        composeRule
            .onNodeWithTag("password")
            .performTextInput("password123")

        // Click login
        composeRule
            .onNodeWithTag("login")
            .performClick()
    }

}
