package com.example.attendanceapp.AuthModels

// This class models the response from both login and register endpoints
data class UserResponse(
    val token: String?, // Token might not be present on register, so it's nullable
    val user: User
)


data class RegisterResponse(
    val message: String?

)
