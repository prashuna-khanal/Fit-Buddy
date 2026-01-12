package com.example.fit_buddy.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.fit_buddy.model.FeedPost
import com.example.fit_buddy.model.FriendRequest
import com.example.fit_buddy.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PostRepository(private val context: Context) {
//    database connections
    private val firebaseUrl = "https://fitbuddy-18168-default-rtdb.firebaseio.com"
    private val firebaseInstance = FirebaseDatabase.getInstance(firebaseUrl)

    private val database = firebaseInstance.getReference("posts")
    private val requestDatabase = firebaseInstance.getReference("friendRequests")
    private val friendsDatabase = firebaseInstance.getReference("friends")
    private val usersDatabase = firebaseInstance.getReference("users")
//    adding cloudinary

    init {
        try {

            val config = mapOf(
                "cloud_name" to "dae6dpgmq",
                "api_key" to "337966462713852",
                "api_secret" to "BXsKEglSz2k7j57i7QLTKKBknuI"
            )
            MediaManager.init(context, config)
        } catch (e: Exception) {
            Log.d("PostRepository", "Cloudinary already initialized: ${e.message}")
        }
    }

//adding friend or requested
    fun getFriendshipStatus(myUserId: String, targetUserId: String): Flow<String> =
        callbackFlow {
            val friendRef = friendsDatabase.child(myUserId).child(targetUserId)
            val requestRef = requestDatabase.child(targetUserId).child(myUserId)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // check if they are already friends
                    friendRef.get().addOnSuccessListener { friendSnap ->
                        if (friendSnap.exists()) {
                            trySend("friends")
                        } else {
                            //if no friends check if a request is pending
                            requestRef.get().addOnSuccessListener { reqSnap ->
                                if (reqSnap.exists()) trySend("requested") else trySend("none")
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }

//           seeing changes so button update instantly
            friendsDatabase.child(myUserId).addValueEventListener(listener)
            requestDatabase.child(targetUserId).addValueEventListener(listener)

            awaitClose {
                friendsDatabase.child(myUserId).removeEventListener(listener)
                requestDatabase.child(targetUserId).removeEventListener(listener)
            }
        }
// count total friend
    fun getFriendCount(userId: String): Flow<Int> = callbackFlow {
        val userFriendsRef = friendsDatabase.child(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //  count how many friends with the logged in user
                val count = snapshot.childrenCount.toInt()
                trySend(count)
            }

            override fun onCancelled(error: DatabaseError) {
//                solving error
                trySend(0)
            }
        }

        userFriendsRef.addValueEventListener(listener)
        awaitClose { userFriendsRef.removeEventListener(listener) }
    }
//

    fun getAcceptedFriends(userId: String): Flow<List<FriendRequest>> = callbackFlow {
        val userFriendsRef = friendsDatabase.child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = snapshot.children.mapNotNull { child ->
                    try {
                        child.getValue(FriendRequest::class.java)
                    } catch (e: Exception) {
                        Log.e("PostRepository", "Data Mismatch in Friends: ${child.value}")
                        null
                    }
                }
                trySend(friends)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        userFriendsRef.addValueEventListener(listener)
        awaitClose { userFriendsRef.removeEventListener(listener) }
    }


    fun getFriendRequests(myUsername: String): Flow<List<FriendRequest>> = callbackFlow {
        val userRequestRef = requestDatabase.child(myUsername)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requests = snapshot.children.mapNotNull { child ->
                    try {
                        child.getValue(FriendRequest::class.java)
                    } catch (e: Exception) {
                        Log.e("PostRepository", "Data Mismatch in Requests: ${child.value}")
                        null
                    }
                }
                trySend(requests.reversed())
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        userRequestRef.addValueEventListener(listener)
        awaitClose { userRequestRef.removeEventListener(listener) }
    }


    fun getAllUsers(): Flow<List<UserModel>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
                trySend(users)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        usersDatabase.addValueEventListener(listener)
        awaitClose { usersDatabase.removeEventListener(listener) }
    }

    fun sendFriendRequest(myUserId: String, myFullName: String, myProfilePic: String, targetUserId: String, onComplete: (Boolean) -> Unit) {
        val requestData = mapOf(
            "userId" to myUserId,
            "username" to myFullName,
            "profilePicUrl" to myProfilePic,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis()
        )
        requestDatabase.child(targetUserId).child(myUserId).setValue(requestData)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    fun getCurrentUserId(): String {
        return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    }
//liking post
    fun toggleLike(postId: String, userId: String) {
        val likeRef = database.child(postId).child("likedBy").child(userId)
        likeRef.get().addOnSuccessListener { snapshot ->
//            checking for duplicate likes
            if (snapshot.exists()) likeRef.removeValue() else likeRef.setValue(true)
        }
    }

    fun deletePost(postId: String, onComplete: (Boolean) -> Unit) {
        database.child(postId).removeValue().addOnCompleteListener { onComplete(it.isSuccessful) }
    }
//uploading in cloudinary + firebase
    fun uploadPost(imageUri: Uri, caption: String, username: String, profilePicUrl: String, onComplete: (Boolean) -> Unit) {
//        initially upload in cloudinary
        MediaManager.get().upload(imageUri).unsigned("fit_buddy_preset")
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
//                    getting back the url
                    val imageUrl = resultData?.get("secure_url").toString()
                    val postId = database.push().key ?: return
//                    saving in direbase

                    val postData = mapOf(
                        "id" to postId,
                        "userId" to getCurrentUserId(),
                        "username" to username,
                        "imageUrl" to imageUrl,
                        "caption" to caption,
                        "timestamp" to System.currentTimeMillis(),
                        "profilePic" to profilePicUrl
                    )
                    database.child(postId).setValue(postData).addOnSuccessListener { onComplete(true) }
                }
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onError(requestId: String?, error: ErrorInfo?) { onComplete(false) }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }
//handle accept or delete request
    fun handleFriendRequest(myUserId: String, myUsername: String, myProfilePic: String, request: FriendRequest, accept: Boolean, onComplete: (Boolean) -> Unit) {
        val senderId = request.userId
        val specificRequestRef = requestDatabase.child(myUserId).child(senderId)

        if (accept) {
//            create mutual
            val friendDataForMe = mapOf("userId" to request.userId, "username" to request.username, "profilePicUrl" to request.profilePicUrl, "status" to "friends", "timestamp" to System.currentTimeMillis())
            val friendDataForSender = mapOf("userId" to myUserId, "username" to myUsername, "profilePicUrl" to myProfilePic, "status" to "friends", "timestamp" to System.currentTimeMillis())

            friendsDatabase.child(myUserId).child(senderId).setValue(friendDataForMe).addOnSuccessListener {
                friendsDatabase.child(senderId).child(myUserId).setValue(friendDataForSender).addOnSuccessListener {
//                    after accepting, remove that request from friendrequest screen
                    specificRequestRef.removeValue().addOnCompleteListener { onComplete(it.isSuccessful) }
                }
            }
        } else {
            specificRequestRef.removeValue().addOnCompleteListener { onComplete(it.isSuccessful) }
        }
    }

    fun getPostsByUser(userId: String): Flow<List<FeedPost>> = callbackFlow {
        val query = database.orderByChild("userId").equalTo(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(FeedPost::class.java) }
                trySend(items.reversed())
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    fun getPosts(): Flow<List<FeedPost>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { child ->
                    try { child.getValue(FeedPost::class.java) } catch (e: Exception) { null }
                }
                trySend(items.reversed())
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }
}