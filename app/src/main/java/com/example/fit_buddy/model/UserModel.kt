package com.example.fit_buddy.model

data class UserModel (
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val dob: String = "",
    val profileImage: String? = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    // ✅ Password is usually kept local or handled by Firebase Auth,
    // but included here if your specific logic requires it.
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
            "weight" to weight,
            "height" to height
            // Note: We typically don't upload the password to the database map for security
        )
    }
}