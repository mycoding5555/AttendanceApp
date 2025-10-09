package com.example.attendanceapp.AuthModels

data class User(
    val id: Int,
    val name: String?, // Assuming name can be null initially
    val email: String,
    val token: String
)
