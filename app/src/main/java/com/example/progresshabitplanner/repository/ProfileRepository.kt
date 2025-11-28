package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.UserProfileResponse
import com.example.progresshabitplanner.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
class ProfileRepository(context: Context) {

    private val api = RetrofitClient.getInstance(context)

    suspend fun getMyProfile(): UserProfileResponse {
        return api.getMyProfile()
    }
}
