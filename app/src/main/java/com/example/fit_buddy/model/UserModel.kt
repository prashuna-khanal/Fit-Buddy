package com.example.fit_buddy.model

class UserModel (
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val dob: String = "",
    val profileImage: String? = "",
    val gender: String = "",
    val weight: String = "",
    val password: String = ""
){

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "fullName" to fullName,
            "email" to email,
            "dob" to dob,
            "profileImage" to profileImage,
            "gender" to gender,
            "weight" to weight
        )
    }
}