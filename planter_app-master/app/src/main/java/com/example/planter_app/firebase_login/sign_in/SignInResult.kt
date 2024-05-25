package com.example.planter_app.firebase_login.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureURL: String?
)