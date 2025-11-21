package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.CreateCustomScheduleRequest
import com.example.progresshabitplanner.model.CreateRecurringScheduleRequest
import com.example.progresshabitplanner.model.CreateWeekdayScheduleRequest
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.network.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }

    suspend fun getScheduleById(id: Int): ScheduleResponse {
        return api.getScheduleById(id)
    }

    suspend fun deleteSchedule(id: Int) {
        val response = api.deleteSchedule(id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete schedule: ${response.code()}")
        }
    }

    suspend fun createCustomSchedule(request: CreateCustomScheduleRequest): ScheduleResponse {
        return api.createCustomSchedule(request)
    }

    suspend fun createRecurringSchedule(request: CreateRecurringScheduleRequest): List<ScheduleResponse> {
        return api.createRecurringSchedule(request)
    }

    suspend fun createWeekdaySchedule(request: CreateWeekdayScheduleRequest): List<ScheduleResponse> {
        return api.createWeekdaySchedule(request)
    }
}
