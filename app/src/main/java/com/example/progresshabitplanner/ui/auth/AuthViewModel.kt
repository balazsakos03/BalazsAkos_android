package com.example.progresshabitplanner.ui.auth

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.progresshabitplanner.model.AuthResponse
import com.example.progresshabitplanner.model.UserProfileResponse
import com.example.progresshabitplanner.repository.AuthRepository
import com.example.progresshabitplanner.repository.ProfileRepository
import kotlinx.coroutines.launch
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)
    private val profileRepository = ProfileRepository(application)

    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    private val _profileResult = MutableLiveData<Result<UserProfileResponse>>()
    val profileResult: LiveData<Result<UserProfileResponse>> = _profileResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.register(username, email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    // ÚJ!
    fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getMyProfile()
                _profileResult.postValue(Result.success(profile))
            } catch (e: Exception) {
                _profileResult.postValue(Result.failure(e))
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
