package com.example.fit_buddy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.launch


class UserViewModel(private val repository: UserRepo) : ViewModel() {

    // LiveData to observe changes
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

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
}