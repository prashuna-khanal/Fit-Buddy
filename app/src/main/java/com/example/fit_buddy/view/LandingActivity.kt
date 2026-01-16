package com.example.fit_buddy.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fit_buddy.repository.UserRepoImpl
import com.example.fit_buddy.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // auto login if user registered in firebase5
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
////            ensure email is verified
//            val intent = Intent(this, WorkoutActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }

        setContent {val navController = rememberNavController()
            val userRepo = UserRepoImpl()

//            initialize viewmodel
            val viewModel: UserViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return UserViewModel(userRepo,this@LandingActivity) as T
                    }
                }
            )

            NavHost(navController = navController, startDestination = "landing") {
                composable("landing") { LandingScreen(navController, viewModel) }

//                defining routes
                composable("signin") { LoginScreen(navController, viewModel) }
                composable("signup") { SignUpScreen(navController, viewModel) }

//               need routes for opt and forget password too
            }
        }
    }
}