package com.example.fit_buddy.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application,
    private val repository: UserRepo
) : AndroidViewModel(application) {

    // SharedPreferences — now safe because application is available
    private val sharedPreferences = application.getSharedPreferences(
        "fit_buddy_prefs",
        Context.MODE_PRIVATE
    )


    //              Loading & Error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    fun clearError() {
        _error.value = null
    }

    //              User Data (for profile/BMI/etc)

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    // Initialize by loading the current user immediately
    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        val userId = repository.getCurrentUserId() ?: return
        viewModelScope.launch {
            repository.getUserData(userId).collect { userData ->
                _user.postValue(userData)
            }
        }
    }

    //              Authentication

    fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        _loading.value = true
        repository.register(email, password) { success, message, userId ->
            _loading.value = false
            if (!success) _error.value = message
            callback(success, message, userId)
        }
    }

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        _loading.value = true
        repository.login(email, password) { success, message ->
            _loading.value = false
            if (!success) _error.value = message
            callback(success, message)
        }
    }

    fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        _loading.value = true
        repository.addUserToDatabase(userId, userModel) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }


    //              Profile Management

    fun getCurrentUserId(): String? = repository.getCurrentUserId()

    fun getUserData(userId: String): Flow<UserModel?> =
        repository.getUserData(userId)

    fun updateUserProfile(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        _loading.value=true
        repository.updateUserProfile(userId, userModel){
            success, message ->
            _loading.value=false
            if(success){
                loadCurrentUser() //refresh

            }
            callback(success,message)
        }
    }

    fun uploadProfileImage(
        imageUri: Uri,
        callback: (Boolean, String) -> Unit
    ) {
        val userId = getCurrentUserId() ?: return
        _loading.value = true
        repository.uploadProfileImage(userId, imageUri){
            success, message ->
            _loading.value=false
            if(success){
                loadCurrentUser()
                callback(true,"Profile picture updated successfully!")
            }else{
                _error.value=message
                callback(false,message)
            }
        }
    }
    fun updateProfileWithCloudinary(cloudinaryUrl: String) {
        val userId = getCurrentUserId() ?: return
        _loading.value = true
        repository.updateProfileImageLink(userId, cloudinaryUrl) { success, message ->
            _loading.value = false
            if (success) {
                loadCurrentUser()
            } else {
                _error.value = message
            }
        }
    }
    fun updateProfileWithUrl(imageUrl: String, callback: (Boolean, String) -> Unit) {
        val userId = getCurrentUserId() ?: return
        _loading.value = true

        repository.updateProfileImageLink(userId, imageUrl) { success, msg ->
            _loading.value = false
            if (success) {
                loadCurrentUser() // Refresh the local user data
            }
            callback(success, msg)
        }
    }

    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repository.deleteAccount(userId, callback)
    }

    fun logout() {
        repository.logout()
    }


    //              Notifications

    private val _notificationsEnabled = MutableLiveData(
        sharedPreferences.getBoolean("notifications_enabled", true)
    )
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        sharedPreferences.edit()
            .putBoolean("notifications_enabled", enabled)
            .apply()
    }
    fun sendPasswordReset(email: String, callback: (Boolean, String) -> Unit) {
        repository.sendPasswordResetEmail(email, callback)
    }

}