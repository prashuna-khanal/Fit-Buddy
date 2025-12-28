package com.example.fit_buddy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel (val repo: UserRepo) : ViewModel() {
    fun login(email:String,password:String,
              callback:(Boolean,String)-> Unit
    ){
        repo.login(email,password,callback)
    }
    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit){
        repo.register(email,password,callback)
    }
    fun addUserTODatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit){
        repo.addUserTODatabase(userId,model,callback)
    }
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit){
        repo.forgetPassword(email,callback)
    }
    fun updatePassword(userId: String, model: UserModel, callback: (Boolean, String) -> Unit){
        repo.updatePassword(userId,model,callback)
    }
    fun getCurrentUser(): FirebaseUser?{
        return repo.getCurrentUser()
    }
    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit){
        repo.deleteAccount(userId,callback)
    }
    fun logout(callback: (Boolean, String) -> Unit){
        repo.logout(callback)
    }
    private val  _users = MutableLiveData<UserModel?>()
    val  users : MutableLiveData<UserModel?>
        get() = _users

    private val _allUsers = MutableLiveData<List<UserModel?>?>()
    val allUsers : MutableLiveData<List<UserModel?>?>
        get() = _allUsers

    private val _loading= MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

    fun getUserById(userId: String) {
        _loading.postValue(true)
        repo.getUserById(userId) { success, msg, data ->
            if (success) {
                _loading.postValue(false)
                _users.postValue(data)
            } else {
                _loading.postValue(false)
                _users.postValue(null)

            }
        }

    }
    fun getAllUser(callback: (Boolean, String, List<UserModel?>?) -> Unit){
        _loading.postValue(true)
        repo.getAllUser {
                success, msg, data ->
            if(success){
                _loading.postValue(false)
                _allUsers.postValue(data)
            }
            else{
                _loading.postValue(false)
                _users.postValue(null)
            }
        }
    }
}