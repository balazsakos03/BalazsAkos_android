package com.example.progresshabitplanner.network

import com.example.progresshabitplanner.model.AuthRequest
import com.example.progresshabitplanner.model.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @Multipart
    @POST("/auth/local/signup")
    suspend fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part profileImage: MultipartBody.Part? = null
    ): Response<AuthResponse>
}