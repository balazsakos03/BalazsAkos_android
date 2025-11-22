package com.example.progresshabitplanner.ui.schedule

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.*
import com.example.progresshabitplanner.repository.ScheduleRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScheduleRepository(application)

    private val _createResult = MutableLiveData<Result<Any>>()
    val createResult: LiveData<Result<Any>> = _createResult

    private val _schedule = MutableLiveData<List<ScheduleResponse>>()
    val schedule: LiveData<List<ScheduleResponse>> = _schedule

    fun loadSchedulesForToday() {
        viewModelScope.launch {
            try {
                val result = repository.getScheduleByDay("today")
                _schedule.postValue(result)
            } catch (e: Exception) {
                _schedule.postValue(emptyList())
            }
        }
    }

    fun createCustomSchedule(request: CreateCustomScheduleRequest) {
        viewModelScope.launch {
            try {
                val result = repository.createCustomSchedule(request)
                _createResult.postValue(Result.success(result))
                Log.d("ScheduleViewModel", "Schedule created: $result")
            } catch (e: Exception) {
                _createResult.postValue(Result.failure(e))
                Log.e("ScheduleViewModel", "Schedule creation failed: ${e.message}", e)
            }
        }
    }

    fun createRecurringSchedule(request: CreateRecurringScheduleRequest) {
        viewModelScope.launch {
            try{
                val result = repository.createRecurringSchedule(request)
                _createResult.postValue(Result.success(result))
            }catch(e: Exception){}
        }
    }

    fun createWeekdaySchedule(request: CreateWeekdayScheduleRequest) {
        viewModelScope.launch {
            try {
                val result = repository.createWeekdaySchedule(request)
                _createResult.postValue(Result.success(result))
            } catch (e: Exception) {
                _createResult.postValue(Result.failure(e))
            }
        }
    }
}