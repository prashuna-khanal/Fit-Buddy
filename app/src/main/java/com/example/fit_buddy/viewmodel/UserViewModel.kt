package com.example.fit_buddy.viewmodel


import android.app.Application
import android.content.Context

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.launch

    private val sharedPreferences =
        application.getSharedPreferences("fit_buddy_prefs", Context.MODE_PRIVATE)


    // LiveData to observe changes
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error
//
    private val _workoutMinutes = MutableLiveData<Map<String, Int>>(loadWorkoutData())
    val workoutMinutes: LiveData<Map<String, Int>> get() = _workoutMinutes

    // ================= USER DATA FOR BMI =================

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    fun loadCurrentUser() {
        val userId = repository.getCurrentUserId() ?: return

        viewModelScope.launch {
            repository.getUserData(userId).collect { userData ->
                _user.postValue(userData)
            }
        }
    }



    fun register(email: String, pass: String, callback: (Boolean, String, String) -> Unit) {
        _loading.value = true
        repository.register(email, pass) { success, message, userId ->
            _loading.value = false
            if (!success) _error.value = message
            callback(success, message, userId)
        }
    }


    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repository.addUserToDatabase(userId, userModel) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }


    fun login(email: String, pass: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repository.login(email, pass) { success, message ->
            _loading.value = false
            if (!success) _error.value = message
            callback(success, message)
        }
    }


    fun clearError() {
        _error.value = null
    }
    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }

    fun getUserData(userId: String): Flow<UserModel?> {
        return repository.getUserData(userId)
    }

    fun updateUserProfile(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repository.updateUserProfile(userId, userModel, callback)
    }
    fun uploadProfileImage(
        imageUri: Uri,
        callback: (Boolean, String) -> Unit
    ) {
        val userId = getCurrentUserId() ?: return
        repository.uploadProfileImage(userId, imageUri, callback)
    }

    fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repository.deleteAccount(userId, callback)
    }
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

    fun logout() {
        repository.logout()
    }


}