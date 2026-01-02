package com.example.fit_buddy.repository

import android.content.Context
import android.net.Uri

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
//    reference to real time database
    private val database = FirebaseDatabase.getInstance().getReference("posts")
    fun uploadPost(imageUri: Uri,caption: String,username:String, onComplete:(Boolean)-> Unit){
        MediaManager.get().upload(imageUri).unsigned("fit_buddy_preset").callback(object : UploadCallback{
            override fun onStart(requestId: String?) {

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