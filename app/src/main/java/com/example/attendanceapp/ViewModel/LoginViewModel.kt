package com.example.attendanceapp.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.attendanceapp.AuthModels.UserResponse
import com.example.attendanceapp.Repository.AuthRepository

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserResponse) : LoginState()
    data class Error(val message: String) : LoginState()

    data class User (val id: Int, val name: String?, val email: String, val token: String)
}

class LoginViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val resp = repo.login(email, password)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    if (body != null) {
                        _state.value = LoginState.Success(body)
                    } else {
                        _state.value = LoginState.Error("Empty response")
                    }
                } else {
                    val message = if (resp.code() == 401) "Invalid credentials" else "Server error: ${resp.code()}"
                    _state.value = LoginState.Error(message)
                }
            } catch (t: Throwable) {
                _state.value = LoginState.Error(t.message ?: "Network error")
            }
        }
    }
}