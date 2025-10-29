package network

import model.AuthRequest
import model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/local/signup")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>
}