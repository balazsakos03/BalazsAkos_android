package com.example.progresshabitplanner.ui.home

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScheduleRepository(application)

    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> = _schedules

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTodaySchedules() {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "=== LOADING TODAY'S SCHEDULES ===")

                // 1. Először próbáljuk a direkt endpoint-ot
                Log.d("HomeViewModel", "Step 1: Trying direct endpoint GET /schedule/day")
                val directResult = repository.getTodaySchedules()
                Log.d("HomeViewModel", "Direct endpoint result: ${directResult.size} schedules")

                // 2. Ha 0, akkor próbáljuk a filter módszert
                if (directResult.isEmpty()) {
                    Log.d("HomeViewModel", "Step 2: Direct endpoint returned 0, trying filtered approach")
                    val filteredResult = repository.getTodaySchedulesWithFilter()
                    Log.d("HomeViewModel", "Filtered approach result: ${filteredResult.size} schedules")
                    _schedules.postValue(filteredResult)
                } else {
                    Log.d("HomeViewModel", "Step 2: Direct endpoint successful, using ${directResult.size} schedules")
                    _schedules.postValue(directResult)
                }

                Log.d("HomeViewModel", "=== FINAL RESULT: ${_schedules.value?.size ?: 0} schedules ===")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "ERROR loading today's schedules: ${e.message}", e)
                _schedules.postValue(emptyList())
                _error.postValue("Failed to load schedules: ${e.message}")
            }
        }
    }

    // Új metódus: összes schedule lekérése (debug célra)
    fun loadAllSchedulesForDebug() {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "=== DEBUG: LOADING ALL SCHEDULES ===")
                val allSchedules = repository.getAllSchedules()
                Log.d("HomeViewModel", "DEBUG - Total schedules in system: ${allSchedules.size}")

                allSchedules.forEachIndexed { index, schedule ->
                    Log.d("HomeViewModel", "DEBUG $index - ID: ${schedule.id}, Date: '${schedule.date}', Start: '${schedule.start_time}', Habit: '${schedule.habit.name}'")
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "DEBUG - Failed to load all schedules: ${e.message}")
            }
        }
    }
}