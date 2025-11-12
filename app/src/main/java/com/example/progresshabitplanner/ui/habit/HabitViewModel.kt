package com.example.progresshabitplanner.ui.habit

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.HabitResponse
import com.example.progresshabitplanner.repository.HabitRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitRepository(application)

    private val _habits = MutableLiveData<List<HabitResponse>>()
    val habits: LiveData<List<HabitResponse>> = _habits

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadHabits() {
        viewModelScope.launch {
            try {
                val result = repository.getAllHabits()
                _habits.postValue(result)
            } catch (e: Exception) {
                _error.postValue("Failed to load habits: ${e.message}")
            }
        }
    }
}
