package com.example.fit_buddy.model

import androidx.annotation.DrawableRes

data class FeaturedWorkout(
    val title: String,
    val level: String,
    val duration: String,
    val calories: String,
    @DrawableRes val image: Int
)