package com.jacob.chronowrist.ui.screens.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.chronowrist.data.model.UserModel
import com.jacob.chronowrist.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    var email by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
        errorMessage = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        errorMessage = null
    }

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter both your email and password to continue."
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                authRepository.loginUser(UserModel(email = email, password = password))
                isSuccess = true
            } catch (e: Exception) {
                val msg = e.message ?: ""
                errorMessage = when {
                    msg.contains("Invalid login credentials", ignoreCase = true) || 
                    msg.contains("unauthorized", ignoreCase = true) -> {
                        "The credentials you entered are incorrect. Please try again or, if you don't have an account, feel free to create one below."
                    }
                    msg.contains("Email not confirmed", ignoreCase = true) -> {
                        "Please check your inbox and confirm your email address before logging in."
                    }
                    else -> "We're having trouble signing you in. Please check your connection and try again."
                }
            } finally {
                isLoading = false
            }
        }
    }
    
    fun clearError() {
        errorMessage = null
    }
}