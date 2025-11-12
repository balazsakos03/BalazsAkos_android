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

    fun loadTodaySchedules() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now().toString()
                val result = repository.getScheduleByDay(today)
                _schedules.postValue(result)
                _error.postValue(null)
            } catch (e: Exception) {
                _error.postValue("Failed to load schedules: ${e.message}")
            }
        }
    }
}
