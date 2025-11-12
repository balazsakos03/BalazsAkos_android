package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }
}
