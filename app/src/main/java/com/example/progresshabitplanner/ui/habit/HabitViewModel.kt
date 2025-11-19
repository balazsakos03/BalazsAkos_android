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

    private val _createResult = MutableLiveData<Result<HabitResponse>>()
    val createResult: LiveData<Result<HabitResponse>> = _createResult

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

    fun createHabit(name: String, description: String, categoryId: Int, goal: String){
        viewModelScope.launch {
            try{
                val result = repository.createHabit(name, description, categoryId, goal)
                _createResult.postValue(Result.success(result))
                _error.postValue(null)
            }catch(e: Exception){
                _createResult.postValue(Result.failure(e))
                _error.postValue("Failed to create habit: ${e.message}")
            }
        }
    }
}
