package com.example.fitbuddy.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.fit_buddy.model.PoseModel
import com.example.fitbuddy.repository.PoseRepo

class PoseViewModel(val repo: PoseRepo) : ViewModel() {
    var poseState by mutableStateOf<PoseModel?>(null)
        private set

    fun processFrame(bitmap: Bitmap) {
        poseState = repo.detectPose(bitmap)
    }

}

