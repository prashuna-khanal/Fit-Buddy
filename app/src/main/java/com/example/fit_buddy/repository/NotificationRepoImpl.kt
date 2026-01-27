package com.example.fit_buddy.repository

import com.example.fit_buddy.model.AppNotification
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NotificationRepoImpl : NotificationRepo {
    private val db = FirebaseDatabase.getInstance().reference

    override fun sendNotification(notification: AppNotification) {
        val id = db.push().key ?: return
        val notif = notification.copy(id = id)

        db.child("notifications")
            .child(notification.userId)
            .child(id)
            .setValue(notif)
    }

    override fun getNotifications(userId: String): Flow<List<AppNotification>> = callbackFlow {
        val ref = db.child("notifications").child(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(AppNotification::class.java)
                }.sortedByDescending { it.timestamp }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun markAsRead(userId: String, notificationId: String) {
        db.child("notifications")
            .child(userId)
            .child(notificationId)
            .child("isRead")
            .setValue(true)
    }
}
