package com.example.progresshabitplanner.ui.schedule

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.ScheduleResponse
import com.example.progresshabitplanner.repository.ScheduleRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ScheduleRepository(application)

    private val _schedule = MutableLiveData<ScheduleResponse>()
    val schedule: LiveData<ScheduleResponse> = _schedule

    private val _deleteResult = MutableLiveData<Result<Unit>>()
    val deleteResult: LiveData<Result<Unit>> = _deleteResult

    fun loadSchedule(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getScheduleById(id)
                _schedule.postValue(result)
            } catch (e: Exception) {
                // itt lehetne hiba UI-t is kezelni, ha akarsz
            }
        }
    }

    fun deleteSchedule(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteSchedule(id)
                _deleteResult.postValue(Result.success(Unit))
            } catch (e: Exception) {
                _deleteResult.postValue(Result.failure(e))
            }
        }
    }
}