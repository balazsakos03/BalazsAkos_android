package com.example.progresshabitplanner.ui.home

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

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
            val today = LocalDate.now().toString() // 2025-11-21
            try {
                val result = repository.getScheduleByDay(today)
                _schedules.postValue(result)
            } catch (e: Exception) {
                _schedules.postValue(emptyList())
            }
        }
    }
}
