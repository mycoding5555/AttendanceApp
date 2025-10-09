package com.example.attendanceapp.Repository

import retrofit2.Response
import com.example.attendanceapp.AuthModels.LoginRequest
import com.example.attendanceapp.AuthModels.UserResponse
import com.example.attendanceapp.network.ApiClient
import com.example.attendanceapp.network.ApiService

class AuthRepository(private val api: ApiService = ApiClient.apiService) {
    suspend fun login(email: String, password: String): Response<UserResponse> {
        val req = LoginRequest(email = email, password = password)
        return api.login(req)
    }
}