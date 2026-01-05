package com.example.fit_buddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fit_buddy.repository.PostRepository
import com.example.fit_buddy.repository.UserRepoImpl

class FeedViewModelFactory(private val repository: PostRepository,private val userRepo: UserRepoImpl) : ViewModelProvider.Factory
{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // 1. Added ': T' return type
        // check if the 'modelClass' is a FeedViewModel
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            // return the actual FeedViewModel with the repository
            return FeedViewModel(repository,userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}