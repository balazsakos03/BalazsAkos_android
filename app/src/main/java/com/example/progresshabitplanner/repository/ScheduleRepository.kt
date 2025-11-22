package com.example.progresshabitplanner.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.model.CreateCustomScheduleRequest
import com.example.progresshabitplanner.model.CreateRecurringScheduleRequest
import com.example.progresshabitplanner.model.CreateWeekdayScheduleRequest
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.network.RetrofitClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getTodaySchedules(): List<ScheduleResponse> {
        Log.d("ScheduleRepository", "Fetching TODAY's schedules (no date parameter)")
        return try {
            val response = api.getTodaySchedules()
            Log.d("ScheduleRepository", "Direct endpoint - Successfully fetched ${response.size} schedules for today")

            response.forEach { schedule ->
                Log.d("ScheduleRepository", "Direct - ID: ${schedule.id}, Date: '${schedule.date}', Habit: ${schedule.habit.name}")
            }

            response
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Failed to fetch today's schedules: ${e.message}", e)
            emptyList()
        }
    }

    // ÚJ METÓDUS: Összes schedule lekérése és lokális szűrés
    suspend fun getTodaySchedulesWithFilter(): List<ScheduleResponse> {
        Log.d("ScheduleRepository", "=== FILTERED APPROACH ===")
        return try {
            // 1. Összes schedule lekérése
            val allSchedules = api.getAllSchedules()
            Log.d("ScheduleRepository", "Total schedules in system: ${allSchedules.size}")

            // 2. Mai dátum
            val today = LocalDate.now()
            val todayFormatted = today.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d("ScheduleRepository", "Today's date for filtering: $todayFormatted")

            // 3. Részletes debug minden schedule-ról
            allSchedules.forEachIndexed { index, schedule ->
                Log.d("ScheduleRepository", "SCHEDULE $index - ID: ${schedule.id}, Date: '${schedule.date}', Start: '${schedule.start_time}', Habit: '${schedule.habit.name}'")
            }

            // 4. Szűrés a mai dátumra
            val todaySchedules = allSchedules.filter { schedule ->
                val isToday = schedule.date.contains(todayFormatted) ||
                        schedule.start_time.contains(todayFormatted)
                Log.d("ScheduleRepository", "Checking schedule ${schedule.id}: date='${schedule.date}', matchesToday=$isToday")
                isToday
            }

            Log.d("ScheduleRepository", "Filtered result: ${todaySchedules.size} schedules for today")

            todaySchedules.forEach { schedule ->
                Log.d("ScheduleRepository", "FILTERED - ID: ${schedule.id}, Date: '${schedule.date}', Habit: ${schedule.habit.name}")
            }

            todaySchedules
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Filtered approach failed: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        Log.d("ScheduleRepository", "Fetching schedules for day: $day")
        return try {
            val response = api.getScheduleByDay(day)
            Log.d("ScheduleRepository", "Successfully fetched ${response.size} schedules")
            response
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "API call failed: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getAllSchedules(): List<ScheduleResponse> {
        Log.d("ScheduleRepository", "Fetching ALL schedules")
        return try {
            val response = api.getAllSchedules()
            Log.d("ScheduleRepository", "Successfully fetched ${response.size} total schedules")
            response
        } catch (e: Exception) {
            Log.e("ScheduleRepository", "Failed to fetch all schedules: ${e.message}")
            emptyList()
        }
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