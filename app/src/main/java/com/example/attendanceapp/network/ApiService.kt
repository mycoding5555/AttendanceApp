package com.example.attendanceapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.attendanceapp.AuthModels.LoginRequest
import com.example.attendanceapp.AuthModels.UserResponse
import com.example.attendanceapp.AuthModels.RegisterRequest

interface ApiService {


    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<UserResponse>

    @POST("auth/login") // The relative path to your login endpoint
    suspend fun login(@Body request: LoginRequest): Response<UserResponse>

}