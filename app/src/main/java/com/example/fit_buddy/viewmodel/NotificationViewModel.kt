package com.example.fit_buddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_buddy.repository.NotificationRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NotificationViewModel(
    private val repo: NotificationRepo,
    private val userId: String
) : ViewModel() {

    val notifications = repo.getNotifications(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun markAsRead(notificationId: String) {
        repo.markAsRead(userId, notificationId)
    }
}