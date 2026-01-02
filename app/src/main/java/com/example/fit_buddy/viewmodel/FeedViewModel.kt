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
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.model.samplePosts


import com.example.fit_buddy.repository.PostRepository
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.Dispatchers

class FeedViewModel (private val repository: PostRepository,
    private val userRepo: UserRepo): ViewModel(){

    // current user identifier for filtering logic
//    val currentUserId: String = repository.getCurrentUserId()

 val currentUserId: String = userRepo.getCurrentUserId() ?: "unknown_user"
    // fetched all registered users so they show up in the search list
    val allUsers: LiveData<List<UserModel>> = repository.getAllUsers()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    // send a follow request to the target user
    fun sendFriendRequest(targetUserId: String) {
        repository.sendFriendRequest(currentUserId, targetUserId) { success ->

        }
    }
    // check if i am friends with this person or if a request is pending
    fun getFriendshipStatus(targetUserId: String): LiveData<String> {
        return repository.getFriendshipStatus(currentUserId, targetUserId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }

//    specific user post in grided
    private val _selectedPostId = mutableStateOf<String?>(null)
    val selectedPostId: State<String?> = _selectedPostId

    fun selectPost(postId: String?) {
        _selectedPostId.value = postId
    }
//    fun getCurrentUserId(): String {
//        return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
//    }

    //    showing list of friend request
    val friendRequests: LiveData<List<FriendRequest>> =
        repository.getFriendRequests(currentUserId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    //    logic behind confirm and delete
    fun respondToRequest(requestId:String, accept:Boolean){
        repository.handleFriendRequest(currentUserId,requestId,accept){
                success ->
        }
    }
    //  to handle liking a post
    fun toggleLike(postId: String) {
        repository.toggleLike(postId, currentUserId)
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

        val otherUsersPosts = posts.filter { it.username != currentUserId }


        if (otherUsersPosts.isEmpty()) {
            samplePosts()
        } else {
            otherUsersPosts
        }
    }

    //  specific Profile Page
    val myPosts: LiveData<List<FeedPost>> = repository.getPostsByUser(currentUserId)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    //    ui states ie state variables
    var isUploading by mutableStateOf(false)
    var showUploadSection by mutableStateOf(false)

    fun sharePost(uri: Uri, caption:String, user: String){
        if(uri ==
            Uri.EMPTY) return

        isUploading = true
        repository.uploadPost(uri,caption,currentUserId){
                success ->
            isUploading = false
            if(success) {

                showUploadSection=false
                _selectedPostId.value=null
            }
        }
    }
}