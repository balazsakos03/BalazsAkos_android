package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.HabitResponse
import com.example.progresshabitplanner.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
class HabitRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getAllHabits(): List<HabitResponse> {
        return api.getAllHabits()
    }
}
