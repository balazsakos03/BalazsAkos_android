package com.example.progresshabitplanner.network

import com.example.progresshabitplanner.model.AuthRequest
import com.example.progresshabitplanner.model.AuthResponse
import com.example.progresshabitplanner.model.HabitResponse
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.model.CreateHabitRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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

    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String): List<ScheduleResponse>

    @GET("/habit")
    suspend fun getAllHabits(): List<HabitResponse>

    @POST("/habit")
    suspend fun createHabit(
        @Body request: CreateHabitRequest
    ): HabitResponse
}