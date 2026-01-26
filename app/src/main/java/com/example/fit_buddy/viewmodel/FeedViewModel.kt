package com.example.fit_buddy.viewmodel

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.model.FeedPost
import com.example.fit_buddy.model.FriendRequest
import com.example.fit_buddy.model.UserModel
import com.example.fit_buddy.repository.PostRepository
import com.example.fit_buddy.repository.UserRepo
import kotlinx.coroutines.Dispatchers

class FeedViewModel(
    private val repository: PostRepository,
    private val userRepo: UserRepo
) : ViewModel() {

    //  user identity
    val currentUserId: String = userRepo.getCurrentUserId() ?: "unknown_user"

    val allUsers: LiveData<List<UserModel>> = repository.getAllUsers()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    //automatically get user name
    val currentUserName: LiveData<String> = allUsers.map { users ->
        users?.find { it.userId == currentUserId }?.fullName ?: "User"
    }

    //  Friendship
    val friendRequests: LiveData<List<FriendRequest>> =
        repository.getFriendRequests(currentUserId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val acceptedFriends: LiveData<List<FriendRequest>> =
        repository.getAcceptedFriends(currentUserId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    //list of people who are friends
    private val friendIds: LiveData<List<String>> = acceptedFriends.map { friends ->
        friends.map { it.userId }
    }

    // all post from friends
    val allPosts: LiveData<List<FeedPost>> = repository.getPosts()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    //
    val friendsPosts: LiveData<List<FeedPost>> = MediatorLiveData<List<FeedPost>>().apply {
        fun updateFeed() {
            val posts = allPosts.value ?: emptyList()
            val ids = friendIds.value ?: emptyList()

            // filter  post expcept logged in user
            value = posts.filter { ids.contains(it.userId) }
                .sortedByDescending { it.timestamp ?: 0L }
        }

        addSource(allPosts) { updateFeed() }
        addSource(friendIds) { updateFeed() }
    }

    // mypost
    val myPosts: LiveData<List<FeedPost>> = repository.getPostsByUser(currentUserId)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    // sending friendrequest
    fun sendFriendRequest(targetUserId: String) {
        val currentUser = allUsers.value?.find { it.userId == currentUserId }
        if (currentUser != null) {
            repository.sendFriendRequest(
                myUserId = currentUserId,
                myFullName = currentUser.fullName,
                myProfilePic = currentUser.profileImage ?: "", // matches UserModel
                targetUserId = targetUserId
            ) { /* Toast handle */ }
        }
    }

    fun respondToRequest(request: FriendRequest, accept: Boolean) {
        val currentUser = allUsers.value?.find { it.userId == currentUserId }
        repository.handleFriendRequest(
            myUserId = currentUserId,
            myUsername = currentUser?.fullName ?: "User",
            myProfilePic = currentUser?.profileImage ?: "",
            request = request,
            accept = accept
        ) { }
    }

    fun getFriendshipStatus(targetUserId: String): LiveData<String> {
        return repository.getFriendshipStatus(currentUserId, targetUserId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    // post integration and deletion
    fun toggleLike(postId: String) {
        repository.toggleLike(postId, currentUserId)
    }

    fun deletePost(postId: String) {
        repository.deletePost(postId) {  }
    }
    //upload status checking
    fun sharePost(uri: Uri, caption: String) {
        if (uri == Uri.EMPTY) return

        val currentUser = allUsers.value?.find { it.userId == currentUserId }
        val myName = currentUser?.fullName ?: "User"
        //
        val myPic = currentUser?.profileImage ?: ""

        isUploading = true
        repository.uploadPost(
            imageUri = uri,
            caption = caption,
            username = myName,
            profilePicUrl = myPic,
            onComplete = { success ->
                isUploading = false
                if (success) {
                    showUploadSection = false
                    _selectedPostId.value = null
                }
            }
        )
    }

    // 7. UI States
    var isUploading by mutableStateOf(false)
    var showUploadSection by mutableStateOf(false)
    var selectedFriendId by mutableStateOf("")
        private set

    private val _selectedPostId = mutableStateOf<String?>(null)
    val selectedPostId: State<String?> = _selectedPostId

    // navigations
    fun navigateToFriendProfile(userId: String) {
        selectedFriendId = userId
    }

    fun selectPost(postId: String?) {
        _selectedPostId.value = postId
    }
    //return to post for specific friends after visiting
    fun getPostsByUser(userId: String): LiveData<List<FeedPost>> {
        return repository.getPostsByUser(userId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun getFriendCount(userId: String): LiveData<Int> =
        repository.getFriendCount(userId)
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
}