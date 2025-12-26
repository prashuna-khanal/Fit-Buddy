package com.example.fit_buddy.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.FeedPost

import com.example.fit_buddy.repository.PostRepository

class FeedViewModel (private val repository: PostRepository): ViewModel(){
//    holds list of post fetehed from firebase
    val allPosts : LiveData<List<FeedPost>> = repository.getPosts().asLiveData(viewModelScope.coroutineContext)
//    ui states ie state variables
    var isUploading by mutableStateOf(false)
    var showUploadSection by mutableStateOf(false)
    fun sharePost(uri: Uri, caption:String, user: String){
        isUploading = true
        repository.uploadPost(uri,caption,user){
            success ->
            isUploading = false
            if(success) showUploadSection = false
        }
    }
}