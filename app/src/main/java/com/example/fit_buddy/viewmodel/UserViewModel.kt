package com.example.fit_buddy.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.flow.Flow


class UserViewModel(private val repository: UserRepo) : ViewModel() {

    // LiveData to observe changes
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error


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