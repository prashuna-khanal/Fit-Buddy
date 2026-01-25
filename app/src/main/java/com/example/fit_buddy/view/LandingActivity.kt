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

<<<<<<< HEAD
        // ✅ Auto-login (optional, enable later)
        /*
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, WorkoutActivity::class.java))
            finish()
            return
        }
        */
=======
        // auto login if user registered in firebase5
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
////            ensure email is verified
//            val intent = Intent(this, WorkoutActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }
>>>>>>> 9dcdba9ba4c4d58982fc999bead7c5dd61cef8e2

        setContent {
            val navController = rememberNavController()
            val userRepo = UserRepoImpl()

            val viewModel: UserViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return UserViewModel(userRepo,this@LandingActivity) as T
                    }
                }
            )

            NavHost(
                navController = navController,
                startDestination = "landing"
            ) {
                composable("landing") {
                    LandingScreen(navController, viewModel)
                }
                composable("signin") {
                    LoginScreen(navController, viewModel)
                }
                composable("signup") {
                    SignUpScreen(navController, viewModel)
                }
            }
        }
    }
}
