package com.example.fit_buddy.repository

import com.example.fit_buddy.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    fun login(email:String,password:String,
              callback:(Boolean,String)-> Unit
    )
    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun addUserTODatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit)
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun updatePassword(userId: String, model: UserModel, callback: (Boolean, String) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit)
    fun logout(callback: (Boolean, String) -> Unit)
    fun getUserById(userId: String, callback: (Boolean, String, UserModel?) -> Unit)
    fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit)
}