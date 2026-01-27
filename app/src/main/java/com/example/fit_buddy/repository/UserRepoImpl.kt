package com.example.fit_buddy.repository

import android.net.Uri
import com.example.fit_buddy.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepoImpl : UserRepo {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Welcome Back!")
                } else {
                    callback(false, task.exception?.message ?: "Login Failed")
                }
            }
    }

    override fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUid = auth.currentUser?.uid ?: ""

                    //  firebaseUid into the UserModel before saving
                    val newUser = UserModel(
                        userId = firebaseUid,
                        email = email,
                        fullName = " Name Variable"
                    )

                    // save to users node using the firebaseUid as the key
                    addUserToDatabase(firebaseUid, newUser) { success, msg ->
                        callback(success, msg, firebaseUid)
                    }
                } else {
                    callback(false, task.exception?.message ?: "Failed", "")
                }
            }
    }


    override fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {

        val userRef = db.getReference("users").child(userId)

        userRef.setValue(userModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Profile synced successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to save profile")
                }
            }
    }

    override fun getUserData(userId: String): Flow<UserModel?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        usersRef.child(userId).addValueEventListener(listener)
        awaitClose { usersRef.child(userId).removeEventListener(listener) }
    }

    override fun getAllUsers(): Flow<List<UserModel>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
                trySend(users)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        usersRef.addValueEventListener(listener)
        awaitClose { usersRef.removeEventListener(listener) }
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in")
            return
        }

        // 1️⃣ Delete from database
        usersRef.child(userId).removeValue()
            .addOnSuccessListener {

                // 2️⃣ Delete auth account
                currentUser.delete()
                    .addOnSuccessListener {
                        callback(true, "Account deleted successfully")
                    }
                    .addOnFailureListener {
                        callback(false, it.message ?: "Failed to delete account")
                    }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to remove user data")
            }
    }

    override fun logout() {
        auth.signOut()    }


    override fun updateUserProfile(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        usersRef.child(userId)
            .updateChildren(userModel.toMap())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Profile updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Update failed")
                }
            }
    }
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun uploadProfileImage(
        userId: String,
        imageUri: Uri,
        callback: (Boolean, String) -> Unit
    ) {
        val imageRef = storageRef.child("profile_images/$userId.jpg")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                usersRef.child(userId).child("profileImage").setValue(uri.toString())
                    .addOnCompleteListener {
                        callback(true, "Profile picture updated")
                    }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Upload failed")
            }    }








}