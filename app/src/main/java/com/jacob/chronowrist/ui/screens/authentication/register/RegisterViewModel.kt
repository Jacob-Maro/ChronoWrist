package com.jacob.chronowrist.ui.screens.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.chronowrist.data.model.UserModel
import com.jacob.chronowrist.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    var fullName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    /** True only when both password fields are non-empty AND they differ */
    val passwordMismatch: Boolean
        get() = confirmPassword.isNotEmpty() && password != confirmPassword

    fun onFullNameChange(value: String) { fullName = value }
    fun onEmailChange(value: String) { email = value }
    fun onPasswordChange(value: String) { password = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }
    fun clearError() { errorMessage = null }

    fun register() {
        if (passwordMismatch) return
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill in all fields"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val user = UserModel(
                    fullName = fullName,
                    email = email,
                    password = password
                )
                authRepository.registerUser(user)
                isSuccess = true
            } catch (e: Exception) {
                errorMessage = e.message ?: "An unexpected error occurred"
            } finally {
                isLoading = false
            }
        }
    }
}
