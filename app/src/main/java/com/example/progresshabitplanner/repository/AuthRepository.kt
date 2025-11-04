package com.example.progresshabitplanner.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.Context
import com.example.progresshabitplanner.model.AuthRequest
import com.example.progresshabitplanner.model.AuthResponse
import com.example.progresshabitplanner.network.RetrofitClient
import java.io.File
import retrofit2.Response


class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun login(email: String, password: String) =
        api.login(AuthRequest(email = email, password = password))

    suspend fun register(username: String, email: String, password: String, imageFile: File? = null): Response<AuthResponse> {
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
        }

        return api.register(usernameBody, emailBody, passwordBody, imagePart)
    }

}