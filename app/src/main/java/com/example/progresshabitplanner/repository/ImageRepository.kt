package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.UserProfileResponse
import com.example.progresshabitplanner.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class ImageRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun uploadProfileImage(file: File): UserProfileResponse {
        val reqFile = file.asRequestBody("image/*".toMediaType())

        val part = MultipartBody.Part.createFormData(
            "profileImage",
            file.name,
            reqFile
        )

        return api.uploadProfileImage(part)
    }
}