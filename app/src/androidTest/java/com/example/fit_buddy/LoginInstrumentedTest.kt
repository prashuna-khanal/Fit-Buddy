package com.example.fit_buddy

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fit_buddy.view.LoginScreen
import com.example.fit_buddy.view.SignUpScreen
import com.example.fit_buddy.view.WorkoutScreen
import com.example.fit_buddy.viewmodel.UserViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {}

//    @get:Rule
//    val composeRule = createAndroidComposeRule<TestActivity>() // simple host activity
//
//    private lateinit var viewModel: UserViewModel
//
//    @Before
//    fun setup() {
//        viewModel = UserViewModel(composeRule.activity.application, RealUserRepo())
//
//        composeRule.activity.setContent {
//            val navController = rememberNavController()
//            NavHost(navController, startDestination = "login") {
//                composable("login") { LoginScreen(navController, viewModel) }
//                composable("signup") { SignUpScreen(navController, viewModel) }
//                composable("workout") { WorkoutScreen(navController) }
//            }
//        }
//    }
//
//    @Test
//    fun loginButton_navigatesToWorkout() {
//        // Fill login form
//        composeRule.onNodeWithTag("email").performTextInput("test@gmail.com")
//        composeRule.onNodeWithTag("password").performTextInput("123456")
//
//        // Click login
//        composeRule.onNodeWithTag("login").performClick()
//
//        // Verify we navigated to "workout"
//        composeRule.runOnIdle {
//            val navController = composeRule.activity.findNavController()
//            assert(navController.currentBackStackEntry?.destination?.route == "workout")
//        }
//    }
//
//    @Test
//    fun signupText_navigatesToSignUp() {
//        // Click SignUp text
//        composeRule.onNodeWithTag("signup").performClick()
//
//        // Verify navigation
//        composeRule.runOnIdle {
//            val navController = composeRule.activity.findNavController()
//            assert(navController.currentBackStackEntry?.destination?.route == "signup")
//        }
//    }
//}
