package com.example.fit_buddy.model

class UserModel (
    val userId: String= "",
    val fullName: String= "",
    val email: String= "",
    val password: String= "",
    val dob: String= "",
    val gender: String= "",
    ){
        fun toMap() : Map<String, Any?> {
            return mapOf(
                "userId" to userId,
                "fullName" to fullName,
                "email" to email,
                "dob" to dob,
                "gender" to gender,
            )
        }
}