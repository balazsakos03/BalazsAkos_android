package com.example.progresshabitplanner.ui.auth

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.progresshabitplanner.model.AuthResponse
import com.example.progresshabitplanner.repository.AuthRepository
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }


    private fun handleResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _authResult.postValue(Result.success(response.body()!!))
        } else {
            _authResult.postValue(Result.failure(Exception("Auth failed: ${response.code()}")))
        }
    }
}