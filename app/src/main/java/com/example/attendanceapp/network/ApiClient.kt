package com.example.attendanceapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // IMPORTANT: Replace with your actual base URL
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Create a logging interceptor to see request and response logs
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create an OkHttpClient and add the logging interceptor
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create the Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the ApiService instance from the Retrofit instance
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
