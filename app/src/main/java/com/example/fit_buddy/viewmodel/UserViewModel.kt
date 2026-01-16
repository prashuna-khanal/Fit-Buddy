package com.example.fit_buddy.viewmodel


import android.content.Context

import android.net.Uri

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.flow.Flow


class UserViewModel(private val repository: UserRepo,context: Context) : ViewModel() {



    private val sharedPreferences = context.getSharedPreferences("fit_buddy_prefs", Context.MODE_PRIVATE)
    // LiveData to observe changes
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error
//
    private val _workoutMinutes = MutableLiveData<Map<String, Int>>(loadWorkoutData())
    val workoutMinutes: LiveData<Map<String, Int>> get() = _workoutMinutes

    private val currentUserId = repository.getCurrentUserId()

    //  automatically fetch user data if ID exists

    val user: LiveData<UserModel?> = if (currentUserId != null) {
        repository.getUserData(currentUserId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    fun updateWorkoutTime(day: String, minutes: Int) {
        val currentMap = _workoutMinutes.value?.toMutableMap() ?: mutableMapOf()
        val totalMinutes = (currentMap[day] ?: 0) + minutes
        currentMap[day] = totalMinutes
        _workoutMinutes.value = currentMap
//        save to storage
        saveWorkoutData(day,totalMinutes)
    }
    private fun saveWorkoutData(day: String, totalMinutes: Int) {
        sharedPreferences.edit().putInt(day, totalMinutes).apply()
    }
    private fun loadWorkoutData(): Map<String, Int> {
        val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val savedMap = mutableMapOf<String, Int>()
        days.forEach { day ->
            savedMap[day] = sharedPreferences.getInt(day, 0)
        }
        return savedMap
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

    fun logout() {
        repository.logout()
    }

}