package com.example.fit_buddy.repository

import android.net.Uri
import com.example.fit_buddy.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )

    fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun updateUserProfile(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    )
    fun uploadProfileImage(
        userId: String,
        imageUri: Uri,
        callback: (Boolean, String) -> Unit
    )

    fun getUserData(userId: String): Flow<UserModel?>

    fun getAllUsers(): Flow<List<UserModel>>

    fun getCurrentUserId(): String?
    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    )


    fun logout()

}