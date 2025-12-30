package com.example.fit_buddy.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.FeedPost
import com.example.fit_buddy.model.FriendRequest

import com.example.fit_buddy.repository.PostRepository
import kotlinx.coroutines.Dispatchers

class FeedViewModel (private val repository: PostRepository): ViewModel(){

    // current user identifier for filtering logic
    private val currentUserId = "Sam"


//    specific user post in grided
    private val _selectedPostId = mutableStateOf<String?>(null)
    val selectedPostId: State<String?> = _selectedPostId

    fun selectPost(postId: String?) {
        _selectedPostId.value = postId
    }

    //    showing list of friend request
    val friendRequests: LiveData<List<FriendRequest>> =
        repository.getFriendRequests("Babita")
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    //    logic behind confirm and delete
    fun respondToRequest(requestId:String, accept:Boolean){
        repository.handleFriendRequest("Babita",requestId,accept){
                success ->
        }
    }
    //  to handle liking a post
    fun toggleLike(postId: String, currentLikes: Int) {
        repository.toggleLike(postId, currentLikes)
    }

    //  to delete a post
    fun deletePost(postId: String) {
        repository.deletePost(postId) { success ->
            if (success) {
//               toast
            }
        }
    }

    //    for postcound
    fun getPostsByUser(userId: String): LiveData<List<FeedPost>> {
        return repository.getPostsByUser(userId).asLiveData(viewModelScope.coroutineContext+ Dispatchers.IO)
    }

    //    dyanamic friend count
    fun getFriendCount(userId: String): LiveData<Int> =
        repository.getFriendCount(userId).asLiveData(viewModelScope.coroutineContext+ Dispatchers.IO)

    //    holds list of post feteched from firebase
    val allPosts : LiveData<List<FeedPost>> = repository.getPosts().asLiveData(viewModelScope.coroutineContext+ Dispatchers.IO)

    // main feed for friends only
    val friendsPosts: LiveData<List<FeedPost>> = allPosts.map { posts ->
        posts.filter { it.username != currentUserId }
    }

    //  specific Profile Page
    val myPosts: LiveData<List<FeedPost>> = repository.getPostsByUser(currentUserId)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    //    ui states ie state variables
    var isUploading by mutableStateOf(false)
    var showUploadSection by mutableStateOf(false)

    fun sharePost(uri: Uri, caption:String, user: String){
        if(uri == null ||
            uri == Uri.EMPTY) return

        isUploading = true
        repository.uploadPost(uri,caption,user){
                success ->
            isUploading = false
            if(success) {

                showUploadSection=false
                _selectedPostId.value=null
            }
        }
    }
}