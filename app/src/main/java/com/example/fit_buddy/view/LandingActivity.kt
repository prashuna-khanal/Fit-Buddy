package com.example.fit_buddy.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit_buddy.repository.UserRepoImpl
import com.example.fit_buddy.viewmodel.UserViewModel
import com.example.fit_buddy.viewmodel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Auto-login (optional, enable later)
        /*
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, WorkoutActivity::class.java))
            finish()
            return
        }
        */

        setContent {
            val navController = rememberNavController()

            val viewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(
                    application = application,
                    repository = UserRepoImpl()
                )
            )

            NavHost(
                navController = navController,
                startDestination = "landing"
            ) {
                composable("landing") {
                    LandingScreen(navController, userViewModel)
                }
                composable("signin") {
                    LoginScreen(navController, userViewModel)
                }
                composable("signup") {
                    SignUpScreen(navController, userViewModel)
                }
            }
        }
    }
}