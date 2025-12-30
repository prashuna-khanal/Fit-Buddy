package com.example.fit_buddy.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.fit_buddy.model.FeedPost
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class PostRepository(private val context: Context) {
    //    telling the actual database
    private val firebaseUrl = "https://fitbuddy-18168-default-rtdb.firebaseio.com"
    private val firebaseInstance = FirebaseDatabase.getInstance(firebaseUrl)

    //    reference to real time database

    private val database = firebaseInstance.getReference("posts")
    //    friend request and friends database
    private val requestDatabase = firebaseInstance.getReference("friendRequests")
    private val friendsDatabase = firebaseInstance.getReference("friends")

    // adding cloudinary
    init {
        try {
            val config = mapOf(
                "cloud_name" to "dae6dpgmq",
                "api_key" to "337966462713852",
                "api_secret" to "BXsKEglSz2k7j57i7QLTKKBknuI"
            )
            MediaManager.init(context, config)
        } catch (e: Exception) {
            Log.d("PostRepository", "Cloudinary already initialized")
        }
    }


    fun toggleLike(postId: String, currentLikes: Int) {
        database.child(postId).child("likes").setValue(currentLikes + 1)
    }

    fun deletePost(postId: String, onComplete: (Boolean) -> Unit) {
        database.child(postId).removeValue().addOnCompleteListener { onComplete(it.isSuccessful) }
    }


    fun uploadPost(imageUri: Uri,caption: String,username:String, onComplete:(Boolean)-> Unit){
        MediaManager.get().upload(imageUri).unsigned("fit_buddy_preset").callback(object : UploadCallback{
            override fun onStart(requestId: String?) {
                // Logic correctly moved outside this anonymous callback
            }

            override fun onProgress(
                requestId: String?,
                bytes: Long,
                totalBytes: Long
            ) {

            }

            override fun onSuccess(requestId: String?, resultData: Map<*, *>?){
//                getting secure url frm cloudinary
                val imageUrl = resultData?.get("secure_url").toString()
//                creating unique id for this post in Firebase
                val postId = database.push().key?:return
//                prepare the data map
                val postData = mapOf(
                    "id" to postId,
                    "username" to username,
                    "imageUrl" to imageUrl,
                    "caption" to caption,
                    "timestamp" to System.currentTimeMillis(),
                    "likes" to 0
                )
//                saving to firebase realtime database
                database.child(postId).setValue(postData).addOnSuccessListener {
                    onComplete(true) //this means successfully added
                }
                    .addOnFailureListener {
                        onComplete(false) // this means database error
                    }

            }

            override fun onError(
                requestId: String?,
                error: ErrorInfo?
            ) {
//                cloudinary error message
                println("cloudinary error: ${error?.description}")
                onComplete(false)
            }

            override fun onReschedule(
                requestId: String?,
                error: ErrorInfo?
            ) {
//               this lofic is handled by cloudinary itself
            }
//            this dispatch handles the upload, if not written image never uploads
        }).dispatch()
    }

    //    fetching real time friend request for specific signed user
    fun getFriendRequests(myUsername:String): Flow<List<com.example.fit_buddy.model.FriendRequest>> = callbackFlow {
        val userRequestRef = requestDatabase.child(myUsername)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requests = snapshot.children.mapNotNull {
                    it.getValue(com.example.fit_buddy.model.FriendRequest::class.java)
                }
                trySend(requests.reversed())
            }


            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        userRequestRef.addValueEventListener(listener)
        awaitClose { userRequestRef.removeEventListener(listener) }

    }
    //    confirm or detele
    fun handleFriendRequest(
        myUsername: String,
        senderId: String,
        accept: Boolean,
        onComplete: (Boolean) -> Unit
    ) {
        val specificRequestRef = requestDatabase.child(myUsername).child(senderId)

        if (accept) {
            // add  to the friend list
            friendsDatabase.child(myUsername).child(senderId).setValue(true)
                .addOnSuccessListener {
                    // Once they are added as a friend, we delete the request from the 'pending' list
                    specificRequestRef.removeValue().addOnCompleteListener { task ->
                        onComplete(task.isSuccessful)
                    }
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            // delete the request without adding them to friends
            specificRequestRef.removeValue().addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
        }
    }
    //    for another friend
    fun getPostsByUser(userId: String): Flow<List<FeedPost>> = callbackFlow {
        // username=userId
        val query = database.orderByChild("username").equalTo(userId)
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





    // dyanmic friend count

    fun getFriendCount(userId: String): Flow<Int> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val count = snapshot.child(userId).childrenCount.toInt()
                trySend(count)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        friendsDatabase.addValueEventListener(listener)
        awaitClose { friendsDatabase.removeEventListener(listener) }
    }




    //    fetching real time image data
    fun getPosts(): Flow<List<FeedPost>> = callbackFlow{
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(FeedPost::class.java) }
                trySend(items.reversed())
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) }

        }
        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }

    }

}