package com.example.fit_buddy.repository

import com.example.fit_buddy.model.UserModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit)
    // single user's details
    fun getUserData(userId: String): Flow<UserModel?>
    //  all users FOR search
    fun getAllUsers(): Flow<List<UserModel>>

    //  current logged in User ID
    fun getCurrentUserId(): String?
}